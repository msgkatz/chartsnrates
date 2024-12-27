package com.msgkatz.ratesapp.data.repos.base

import androidx.collection.MutableOrderedScatterSet
import androidx.collection.mutableOrderedScatterSetOf
import com.msgkatz.ratesapp.data.model.Candle
import com.msgkatz.ratesapp.data.model.Interval
import com.msgkatz.ratesapp.data.model.normalizeInSeconds
import com.msgkatz.ratesapp.data.network.websocket.StreamEventTypeWSModel
import com.msgkatz.ratesapp.data.network.websocket.WebSocketDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CurToolRealtimeBalancedV2PriceRepositoryImpl(
    private val wsockds: WebSocketDataSource,
    private val map: MutableMap<String, MutableSet<Candle>>,
    private val intervalsRepository: IntervalListRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
                                                    //.limitedParallelism(1),
) : CurToolRealtimeBalancedPriceRepository {

    private val mutableSharedFlow = MutableSharedFlow<Candle>(replay = 0)
    private val sharedFlow: SharedFlow<Candle> = mutableSharedFlow

    private val candlesInternal: MutableList<Candle> = mutableListOf<Candle>()

    private val exh: CoroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            println("CurToolRealtimeBalancedV2PriceRepositoryImpl err: ${throwable.message ?: throwable.toString()}")
        }
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher + exh)

    private val candlesInternalMutex = Mutex()

    override fun subscribeToolPrice(
        symbol: String,
        interval: String
    ): Flow<Candle> {
        scope.coroutineContext.cancelChildren()
        candlesInternal.clear()

        scope.launch {
            val _interval: Interval = intervalsRepository.getIntervalByName(interval) ?: throw Exception("No interval")
            val name = "${symbol}_${interval}"
            map.get(name)?.let { it.clear() }

            launch {
                var lastTime: Long = -1
                while (isActive) {
                    val candles = map.get("${symbol}_${interval}")
                    candles?.lastOrNull()?.let {
                        var candleToSend = it
                        if (_interval.type == 0) {
                            if (lastTime < 0) {
                                lastTime = it.time
                            } else if (lastTime == it.time) {
                                lastTime += _interval.perItemDefaultMs
                                candleToSend = Candle(candleToSend, lastTime)
                                candles.add(candleToSend)
                            }
                        }
                        mutableSharedFlow.emit(candleToSend)
                    }
                    delay(1000)
                }
            }

            launch {
                wsockds.getKlineAndMiniTickerComboStream(symbol, interval)
                    .map { wsmodel ->
                        val name = "${symbol}_${interval}"
                        var candle: Candle? = null
                        try {
                            val eventType = wsmodel.parseEvent()
                            if (eventType == StreamEventTypeWSModel.TYPE_KLINE) {
                                candle = wsmodel.toKline().toCandle()
                            } else if (eventType == StreamEventTypeWSModel.TYPE_24_TICKER_MINI) {
                                candle = wsmodel.toMarketTickerMini().toCandle()
                            }
                        } catch (e: Exception) {
                            println(e.message ?: e.toString())
                        }

                        val curset = map.getOrPut(name) { LinkedHashSet() }
                        //candle = candle ?: curset?.lastOrNull()
                        candle?.let {
                            if (_interval.type == 0) {
                                curset?.add(it)
                            } else {
                                if (!curset.isEmpty() && it.time < (curset.last().time + _interval.perItemDefaultMs)) {
                                    val toRebase = curset.last()
                                    curset.remove(toRebase)
                                    curset.add(toRebase.rebase(it.priceLow, it.priceHigh, it.priceClose))
                                    mutableSharedFlow.emit(curset.last())
                                } else {
                                    val candleToAdd = Candle(it,
                                        normalizeInSeconds(
                                            _interval.perItemDefaultMs / 1000,
                                            (candle.time / 1000)
                                        ) * 1000
                                    )
                                    curset?.add(candleToAdd)
                                }
                            }

                        }
                    }
            }
        }

        return sharedFlow
    }


    override suspend fun getInterimPrices(
        symbol: String,
        interval: String,
        startTime: Long,
        endTime: Long?,
    ): List<Candle> = coroutineScope {
        val _interval: Interval = intervalsRepository.getIntervalByName(interval) ?: throw Exception("No interval")
        val ret = mutableListOf<Candle>()
        val set: MutableOrderedScatterSet<Candle> = mutableOrderedScatterSetOf<Candle>()

        val name = "${symbol}_${_interval.symbol}"
        candlesInternalMutex.withLock {
            map.get(name)?.let {
                if (!it.isEmpty()) {
                    val fromCandle = Candle(
                        time =
                        normalizeInSeconds(
                            (_interval.perItemDefaultMs / 1000), startTime / 1000
                        ) * 1000 - 1
                    )


                    val toCandle = if (endTime == null) it.last() else Candle(time = endTime)

                    ret.addAll(it.filter { cd -> cd.time > fromCandle.time && cd.time <= toCandle.time })


                }
            }
        }

//        candlesInternalMutex.withLock {
//            ret.addAll(candlesInternal)
//        }
        ret
    }


}