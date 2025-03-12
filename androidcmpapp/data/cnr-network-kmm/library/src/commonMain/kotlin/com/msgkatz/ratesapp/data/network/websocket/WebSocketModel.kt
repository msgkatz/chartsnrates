package com.msgkatz.ratesapp.data.network.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

sealed class StreamEventTypeWSModel(
    val typeName: String
) {
    object TYPE_KLINE : StreamEventTypeWSModel("kline")
    object TYPE_24_TICKER_MINI : StreamEventTypeWSModel("24hrMiniTicker")
    object TYPE_24_TICKER : StreamEventTypeWSModel("24hrTicker")
    object TYPE_UNKNOWN : StreamEventTypeWSModel("NA");

    companion object {
        fun getTypeByName(typeName: String?): StreamEventTypeWSModel {
            return when (typeName) {
                null -> { TYPE_UNKNOWN }
                "kline" -> { TYPE_KLINE }
                "24hrMiniTicker" -> { TYPE_24_TICKER_MINI }
                "24hrTicker" -> { TYPE_24_TICKER }
                else -> { TYPE_UNKNOWN }
            }
        }
    }
}


@Serializable
data class StreamEventWSModel (
    @SerialName("e") val eventType: String,
    @SerialName("E") val eventTime: Long = 0,
    @SerialName("s") var symbol: String
)

@Serializable
data class StreamKlineWSModel (
    @SerialName("t") val timeStart: Long = 0,
    @SerialName("T") val timeEnd: Long = 0,
    @SerialName("s") val symbol: String,
    @SerialName("i") val interval: String,
    @SerialName("o") val open: Double = 0.0,
    @SerialName("c") val close: Double = 0.0,
    @SerialName("h") val high: Double = 0.0,
    @SerialName("l") val low: Double = 0.0,
)

@Serializable
data class StreamKlineEventWSModel (
    @SerialName("e") val eventType: String,
    @SerialName("E") val eventTime: Long = 0,
    @SerialName("s") var symbol: String,
    @SerialName("k") val kline: StreamKlineWSModel,
)

@Serializable
data class StreamMarketTickerMiniWSModel (
    @SerialName("E") val eventTime: Long = 0,
    @SerialName("s") val symbol: String,
    @SerialName("o") val open: Double = 0.0,
    @SerialName("c") val close: Double = 0.0,
    @SerialName("h") val high: Double = 0.0,
    @SerialName("l") val low: Double = 0.0,
)

@Serializable
data class StreamComboBaseWSModel (
    @SerialName("stream") val streamName: String,
    @SerialName("data") val data: JsonObject
)