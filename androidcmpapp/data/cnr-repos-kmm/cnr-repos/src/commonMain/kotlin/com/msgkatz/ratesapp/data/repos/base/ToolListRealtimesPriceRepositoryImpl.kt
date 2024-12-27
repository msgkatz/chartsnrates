package com.msgkatz.ratesapp.data.repos.base

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

    override fun subscribeToolPrices(): Flow<Map<String, Set<PriceSimple>>> {
        return wsockds.getMiniTickerStreamAll()
            .map { list ->
                val newmap: MutableMap<String, Set<PriceSimple>> = mutableMapOf()

                list.map {
                    val tool = toolRepository.getToolMap()?.get(it.symbol)
                    PriceSimple(tool!!, it.close)
                }.groupBy {
                    it.tool.quoteAsset.nameShort
                }.map {

                    toolRepository.getQuoteAssetMap()?.get(it.key)?.let { asset ->
                        val set: MutableSet<PriceSimple> = mutableSetOf<PriceSimple>()
                        set.addAll(it.value.sortedBy { s -> s.price })
                        newmap.put(asset.nameShort, set)
                    }

                }
                if (!newmap.isEmpty()) toolListPriceRepository.updateMultiMap(newmap)

                newmap
        }
    }
}