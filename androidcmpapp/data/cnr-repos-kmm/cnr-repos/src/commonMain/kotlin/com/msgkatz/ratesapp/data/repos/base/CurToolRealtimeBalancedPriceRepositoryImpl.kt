package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Candle
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
import kotlin.math.floor

class CurToolRealtimeBalancedPriceRepositoryImpl(
    private val wsockds: WebSocketDataSource,
    private val map: MutableMap<String, MutableSet<Candle>>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
        //.limitedParallelism(1),
) : CurToolRealtimeBalancedPriceRepository {

    private val mutableSharedFlow = MutableSharedFlow<Candle>(replay = 0)
    private val sharedFlow: SharedFlow<Candle> = mutableSharedFlow

    private val candlesInternal: MutableList<Candle> = mutableListOf<Candle>()

    private val exh: CoroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            println("CurToolRealtimeBalancedPriceRepositoryImpl err: ${throwable.message ?: throwable.toString()}")
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
            launch {
                while (isActive) {
                    val candles = map.get("${symbol}_${interval}")
                    candles?.lastOrNull()?.let {
                        mutableSharedFlow.emit(it)
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
                        candle?.let { curset?.add(it) }
                    }
            }
        }

        return sharedFlow
    }

    private suspend fun addCandleInternal(candle: Candle) = coroutineScope {
        candlesInternalMutex.withLock {
            if (candlesInternal.isEmpty()) {
                val extraCandlesCount: Int =
                    (((candle.time / 1000) as Long) - normalizeInSeconds(
                        60,
                        ((candle.time / 1000) as Long)
                    )).toInt()
                for (i in 0 until extraCandlesCount) candlesInternal.add(candle)
            }
            candlesInternal.add(candle)
        }
    }



    override suspend fun getInterimPrices(symbol: String,
                                        interval: String,
                                        startTime: Long,
                                        endTime: Long?,
    ): List<Candle> = coroutineScope {
        val ret = mutableListOf<Candle>()
        candlesInternalMutex.withLock {
            ret.addAll(candlesInternal)
        }
        ret
    }


}

