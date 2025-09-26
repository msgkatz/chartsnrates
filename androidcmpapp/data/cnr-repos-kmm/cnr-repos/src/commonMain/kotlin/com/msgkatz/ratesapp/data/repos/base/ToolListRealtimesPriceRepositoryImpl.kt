package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PriceSimple
import com.msgkatz.ratesapp.data.network.websocket.WebSocketDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlin.time.ComparableTimeMark
import kotlin.time.TestTimeSource

class ToolListRealtimesPriceRepositoryImpl(
    private val wsockds: WebSocketDataSource,
    private val toolRepository: ToolRepository,
    private val toolListPriceRepository: ToolListPriceRepository,
    private val ioDispatcher: CoroutineDispatcher
): ToolListRealtimesPriceRepository {
    private val mutex: Mutex = Mutex()
    private val timeSource = TestTimeSource()
    private var timeMark: ComparableTimeMark? = null //timeSource.markNow()
    private var multimap: Map<String, Set<PriceSimple>> = emptyMap()

    //override
    fun subscribeToolPrices2(): Flow<Map<String, Set<PriceSimple>>> {
        return wsockds.getMiniTickerStreamAll()
            .map { list ->
                val newmap: MutableMap<String, Set<PriceSimple>> = mutableMapOf()
                val multimap = toolListPriceRepository.getMultiMap()
                list.mapNotNull {
                    toolRepository.getToolMap()?.get(it.symbol)?.let { tool ->
                        PriceSimple(tool, it.close)
                    }
                }.groupBy {
                    it.tool.quoteAsset.nameShort
                }.map {

                    toolRepository.getQuoteAssetMap()?.get(it.key)?.let { asset ->
                        val set: MutableSet<PriceSimple> = mutableSetOf<PriceSimple>()
                        multimap.get(asset.nameShort)?.let {
                            set.addAll(it)
                        }
                        set.removeAll(it.value)
                        set.addAll(it.value)

                        newmap.put(asset.nameShort, set.sorted().toSet())
                    }

                }
                if (!newmap.isEmpty()) toolListPriceRepository.updateMultiMap(newmap)

                newmap
        }
    }

    override fun subscribeToolPrices(): Flow<Map<String, Set<PriceSimple>>> {
        return wsockds.getMiniTickerStreamAll()
            .map { tickerList ->
                val currentPricesByQuoteAsset = toolListPriceRepository.getMultiMap()
                val tools = toolRepository.getToolMap() ?: emptyMap()
                val quoteAssets = toolRepository.getQuoteAssetMap() ?: emptyMap()

                val newPricesSimple = tickerList.mapNotNull { ticker ->
                    tools[ticker.symbol]?.let { tool ->
                        PriceSimple(tool, ticker.close)
                    }
                }

                val updatedPricesByQuoteAsset = updatePrices(currentPricesByQuoteAsset, newPricesSimple, quoteAssets)

                if (updatedPricesByQuoteAsset.isNotEmpty()) { // Or check if it actually changed
                    toolListPriceRepository.updateMultiMap(updatedPricesByQuoteAsset)
                }
                updatedPricesByQuoteAsset
            }
        // .catch { emit(emptyMap()) } // Example error handling
        // .flowOn(ioDispatcher) // If the mapping is CPU intensive
    }

    private fun updatePrices(
        currentPrices: Map<String, Set<PriceSimple>>,
        newPrices: List<PriceSimple>,
        quoteAssets: Map<String, Asset> // Assuming QuoteAsset is the type in getQuoteAssetMap
    ): MutableMap<String, Set<PriceSimple>> {
        val result = currentPrices.toMutableMap() // Start with a copy of current prices

        newPrices.groupBy { it.tool.quoteAsset.nameShort }
            .forEach { (quoteAssetName, pricesForAsset) ->
                quoteAssets[quoteAssetName]?.let { asset -> // Ensure quote asset exists
                    val existingAssetPrices = result[asset.nameShort]?.toMutableSet() ?: mutableSetOf()

                    // More robust update logic:
                    // Assuming PriceSimple's equals/hashCode is based on the 'tool' for uniqueness within the set
                    val pricesToAddOrUpdate = pricesForAsset.toSet() // Remove duplicates from incoming batch first

                    // Remove old versions of prices that are being updated
                    // This requires PriceSimple to have a proper equals/hashCode based on its identifying tool
                    val toolsInNewBatch = pricesToAddOrUpdate.map { it.tool }.toSet()
                    existingAssetPrices.removeAll { it.tool in toolsInNewBatch }

                    existingAssetPrices.addAll(pricesToAddOrUpdate)

                    // If you need a specific order, sort before converting to Set if your consumer
                    // relies on an ordered collection that you then convert to Set.
                    // Otherwise, Set itself doesn't guarantee order.
                    // If order is crucial, perhaps a List<PriceSimple> is more appropriate.
                    result[asset.nameShort] = existingAssetPrices.sorted().toSet() // Be mindful of Set ordering
                }
            }
        return result
    }
}