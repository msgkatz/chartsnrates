package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.PriceSimple
import com.msgkatz.ratesapp.data.network.rest.RestDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.ComparableTimeMark
import kotlin.time.DurationUnit
import kotlin.time.TestTimeSource

class ToolListPriceRepositoryImpl(
    private val networkds: RestDataSource,
    private val toolRepository: ToolRepository,
    private val ioDispatcher: CoroutineDispatcher
): ToolListPriceRepository {
    private val mutex: Mutex = Mutex()
    private val timeSource = TestTimeSource()
    private var timeMark: ComparableTimeMark? = null //timeSource.markNow()
    private var multimap: Map<String, Set<PriceSimple>> = emptyMap()

    override suspend fun getToolPrices(): Map<String, Set<PriceSimple>> = coroutineScope {
        if (isLastUpdatedRecently()) {
            return@coroutineScope getMultiMap() //multimap
        } else {
            val prices = networkds.getPriceSimple()
            if (prices.isFailure || prices.getOrNull() == null) {
                return@coroutineScope emptyMap()
            } else {
                val newmap: MutableMap<String, Set<PriceSimple>> = mutableMapOf()
                val _multimap = getMultiMap()
                prices.getOrNull()?.let { list ->
                    list.map {
                        val tool = toolRepository.getToolMap()?.get(it.instrumentSymbol)
                        PriceSimple(tool!!, it.price)
                    }.groupBy {
                        it.tool.quoteAsset.nameShort
                    }.map {
                        toolRepository.getQuoteAssetMap()?.get(it.key)?.let { asset ->
                            val set: MutableSet<PriceSimple> = mutableSetOf<PriceSimple>()
                            _multimap.get(asset.nameShort)?.let {
                                set.addAll(it)
                            }
                            set.removeAll(it.value)
                            set.addAll(it.value)

                            newmap.put(asset.nameShort, set.sorted().toSet())
                        }
                    }
                }
//                timeMark = timeSource.markNow()
//                multimap = newmap
                updateMultiMap(newmap)
                return@coroutineScope getMultiMap() //multimap
            }
        }
    }

    override suspend fun updateMultiMap(newmap: MutableMap<String, Set<PriceSimple>>) = coroutineScope {
        mutex.withLock {
            timeMark = timeSource.markNow()
            multimap = newmap
        }
    }

    override suspend fun getMultiMap(): Map<String, Set<PriceSimple>> {
        mutex.withLock {
            return multimap
        }
    }

    private suspend fun isLastUpdatedRecently(): Boolean = mutex.withLock {
        if (timeMark == null) return false
        val newMark = timeSource.markNow()
        val delta = newMark.minus(timeMark!!).toLong(DurationUnit.MILLISECONDS)
        return delta <= CACHED_PERIOD_PARAM_MS
    }

    companion object {
        private const val CACHED_PERIOD_PARAM_MS = (60 * 1000).toLong()
    }

}