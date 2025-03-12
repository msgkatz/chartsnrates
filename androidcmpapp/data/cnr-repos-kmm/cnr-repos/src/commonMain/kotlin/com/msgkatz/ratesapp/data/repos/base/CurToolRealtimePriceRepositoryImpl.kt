package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Candle
import com.msgkatz.ratesapp.data.model.IntervalLocalJSON
import com.msgkatz.ratesapp.data.network.websocket.StreamComboBaseWSModel
import com.msgkatz.ratesapp.data.network.websocket.StreamEventTypeWSModel
import com.msgkatz.ratesapp.data.network.websocket.StreamEventWSModel
import com.msgkatz.ratesapp.data.network.websocket.StreamKlineEventWSModel
import com.msgkatz.ratesapp.data.network.websocket.StreamKlineWSModel
import com.msgkatz.ratesapp.data.network.websocket.StreamMarketTickerMiniWSModel
import com.msgkatz.ratesapp.data.network.websocket.WebSocketDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class CurToolRealtimePriceRepositoryImpl(
    private val wsockds: WebSocketDataSource,
    private val map: MutableMap<String, MutableSet<Candle>>
    //private val intervalRepo: IntervalListRepository,
): CurToolRealtimePriceRepository {

    //private val map: MutableMap<String, MutableSet<Candle>> = HashMap()
    private var curset: MutableSet<Candle>? = null //LinkedHashSet()

    override fun subscribeToolPriceCombo(symbol: String, interval: String): Flow<Candle?> =
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

                curset = map.getOrPut(name) { LinkedHashSet() }
                candle = candle ?: curset?.lastOrNull()
                candle?.let { curset?.add(it) }

                candle
            }
}

fun StreamComboBaseWSModel.parseEvent(): StreamEventTypeWSModel {
    val withUnknownKeys = Json { ignoreUnknownKeys = true }
    return StreamEventTypeWSModel.getTypeByName(withUnknownKeys.decodeFromJsonElement<StreamEventWSModel>(this.data).eventType)
}

fun StreamComboBaseWSModel.toKline(): StreamKlineEventWSModel {
    val withUnknownKeys = Json { ignoreUnknownKeys = true }
    return withUnknownKeys.decodeFromJsonElement<StreamKlineEventWSModel>(this.data)
}

fun StreamComboBaseWSModel.toMarketTickerMini(): StreamMarketTickerMiniWSModel {
    val withUnknownKeys = Json { ignoreUnknownKeys = true }
    return withUnknownKeys.decodeFromJsonElement<StreamMarketTickerMiniWSModel>(this.data)
}

fun StreamKlineEventWSModel.toCandle(): Candle = Candle(this.kline.open, this.kline.high, this.kline.low, this.kline.close, this.eventTime)
fun StreamMarketTickerMiniWSModel.toCandle(): Candle = Candle(this.open, this.high, this.low, this.close, this.eventTime)
