package com.msgkatz.ratesapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject


/**************************************************************************************************
 * From Local JSON
 */
@Serializable
data class IntervalLocalJSON(
    @SerialName("id") val id: Int = 0,
    @SerialName("type") val type: Int = 0,
    @SerialName("symbol") val symbol: String? = null,
    @SerialName("apiSymbol") val symbolApi: String? = null,
    @SerialName("perItemDefaultMs") val perItemDefaultMs: Long = 0,
    @SerialName("perBlockDefaultMs") val perBlockDefaultMs: Long = 0,
    @SerialName("perItemMinMs") val perItemMinMs: Long = 0,
    @SerialName("perBlockMinMs") val perBlockMinMs: Long = 0,
    @SerialName("perItemMaxMs") val perItemMaxMs: Long = 0,
    @SerialName("perBlockMaxMs") val perBlockMaxMs: Long = 0,
    @SerialName("inUse") val inUse: Int = 0
)

/**************************************************************************************************
 * From REST
 */
const val BASE_URL = "https://www.binance.com/"
const val BASE_URL_TO_CHANGE = "ex.bnbstatic.com/images"
const val BASE_URL_TO_CHANGE_TO = "www.binance.com/file/resources/img"

@Serializable
data class AssetDomain(
    val id: Int = 0,
    val nameShort: String,
    val nameLong: String? = null,
    val logoShortUrl: String? = null,
    val logoFullUrl: String? = null
) {
    var innerLogoFullUrl: String? = null
    fun getLogoFullUrlM(): String? {

        if (innerLogoFullUrl == null) {
            if (logoShortUrl != null && logoShortUrl.contains("http")) {
                innerLogoFullUrl = logoShortUrl
            } else {
                innerLogoFullUrl = "${BASE_URL}${logoShortUrl}"
            }

            if (innerLogoFullUrl != null && innerLogoFullUrl?.contains(BASE_URL_TO_CHANGE) == true) {
                innerLogoFullUrl =
                    innerLogoFullUrl?.replace(BASE_URL_TO_CHANGE, BASE_URL_TO_CHANGE_TO)
            }
        }

        return innerLogoFullUrl
    }
}


/**
 * Tools are pairs of assets, like ETHBTC, etc.
 */
@Serializable
data class ToolDTDomain(
    val symbol: String? = null,
    val baseAsset: String? = null,
    val basePrecision: Int = 0,
    val quoteAsset: String? = null,
    val quotePrecision: Int = 0,
    val status: String? = null
)

@Serializable
data class ServerTimeDomain(
    val serverTime: Long = 0
)

@Serializable
data class PriceSimpleDTDomain(
    val instrumentSymbol: String? = null,
    val price: Double = 0.0
)


/**
 * Price By Ticker
 *
"symbol": "BNBBTC",
"priceChange": "-94.99999800",
"priceChangePercent": "-95.960",
"weightedAvgPrice": "0.29628482",
"prevClosePrice": "0.10002000",
"lastPrice": "4.00000200",
"lastQty": "200.00000000",
"bidPrice": "4.00000000",
"askPrice": "4.00000200",
"openPrice": "99.00000000",
"highPrice": "100.00000000",
"lowPrice": "0.10000000",
"volume": "8913.30000000",
"quoteVolume": "15.30000000",
"openTime": 1499783499040,
"closeTime": 1499869899040,
"firstId": 28385,   // First tradeId
"lastId": 28460,    // Last tradeId
"count": 76         // Trade count

 */
@Serializable
data class PriceByTickerDomain(
    val symbol: String? = null,
    val prevClosePrice: Double = 0.0,
    val lastPrice: Double = 0.0,
    val openPrice: Double = 0.0,
    val highPrice: Double = 0.0,
    val lowPrice: Double = 0.0,
    val openTime: Long = 0,
    val closeTime: Long = 0
)

/**
 * Price By Candle
 *
1499040000000,     // Open time - 0 [@param openTime]
"0.01634790",       // Open - 1
"0.80000000",       // High - 2
"0.01575800",       // Low - 3
"0.01577100",       // Close - 4
"148976.11427815",  // Volume - 5
1499644799999,      // Close time - 6
"2434.19055334",    // Quote asset volume - 7
308,                // Number of trades - 8
"1756.87402397",    // Taker buy base asset volume - 9
"28.46694368",      // Taker buy quote asset volume - 10
"17928899.62484339" // Ignore - 11

 */
@Serializable
data class PriceByCandleDomain (
    val openTime: Long = 0,
    val open: Double = 0.0,
    val high: Double = 0.0,
    val low: Double = 0.0,
    val close: Double = 0.0,

    val volume: Double = 0.0,
    val closeTime: Long = 0,
    val quoteAssetVolume: Double = 0.0,
    val tradesNum: Long = 0,
    val takerBuyBaseAssetVolume: Double = 0.0,
    val takerBuyQuoteAssetVolume: Double = 0.0,
    val ignore: Double = 0.0
)

@Serializable
data class PlatformInfoDTDomain(
    val timeZone: String? = null,
    val serverTime: Long = 0,
    val toolList: List<ToolDTDomain>? = null
)

/**************************************************************************************************
 * From WS
 */

@Serializable
data class StreamEventDomain (
    val eventType: String,
    val eventTime: Long = 0,
    var symbol: String
)

@Serializable
data class StreamKlineDomain (
    val timeStart: Long = 0,
    val timeEnd: Long = 0,
    val symbol: String,
    val interval: String,
    val open: Double = 0.0,
    val close: Double = 0.0,
    val high: Double = 0.0,
    val low: Double = 0.0,
)

@Serializable
data class StreamKlineEventDomain (
    val eventType: String,
    val eventTime: Long = 0,
    var symbol: String,
    val kline: StreamKlineDomain,
)

@Serializable
data class StreamMarketTickerMiniDomain (
    val eventTime: Long = 0,
    val symbol: String,
    val open: Double = 0.0,
    val close: Double = 0.0,
    val high: Double = 0.0,
    val low: Double = 0.0,
)

@Serializable
data class StreamComboBaseDomain (
    val streamName: String,
    val data: JsonObject
)


/**************************************************************************************************
 **************************************************************************************************
 * From upper layers
 **************************************************************************************************
 */

/**
 * from domain
 */

@Serializable
data class Asset(
    val id: Int = 0,
    val nameShort: String,
    val nameLong: String? = null,
    val logoShortUrl: String? = null,
    val logoFullUrl: String? = null
) {
    var innerLogoFullUrl: String? = null
    fun getLogoFullUrlM(): String? {

        if (innerLogoFullUrl == null) {
            if (logoShortUrl != null && logoShortUrl.contains("http")) {
                innerLogoFullUrl = logoShortUrl
            } else {
                innerLogoFullUrl = "${BASE_URL}${logoShortUrl}"
            }

            if (innerLogoFullUrl != null && innerLogoFullUrl?.contains(BASE_URL_TO_CHANGE) == true) {
                innerLogoFullUrl =
                    innerLogoFullUrl?.replace(BASE_URL_TO_CHANGE, BASE_URL_TO_CHANGE_TO)
            }
        }

        return innerLogoFullUrl
    }
}

data class Tool(
    val name: String,
    val baseAsset: Asset,
    val quoteAsset: Asset,
    val isActive: Boolean
): Comparable<Tool> {
    override fun compareTo(other: Tool): Int {
        return baseAsset.nameShort.compareTo(other.baseAsset.nameShort)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Tool) return false
        val o: Tool = other as Tool
        return (name == o.name)
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

data class Interval (
    val id: Int = 0,
    val type: Int = 0,
    val symbol: String? = null,
    val symbolApi: String? = null,
    val perItemDefaultMs: Long = 0,
    val perBlockDefaultMs: Long = 0,
    val perItemMinMs: Long = 0,
    val perBlockMinMs: Long = 0,
    val perItemMaxMs: Long = 0,
    val perBlockMaxMs: Long = 0,
    val inUse: Int = 0,
    val selected: Boolean = false,
)

data class PlatformInfo(
    val timeZone: String?, 
    val serverTime: Long
)

data class PriceSimple(
    val tool: Tool, 
    val price: Double
) : Comparable<PriceSimple> {
    override fun compareTo(other: PriceSimple): Int {
        return tool.baseAsset.nameShort.compareTo(other.tool.baseAsset.nameShort)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is PriceSimple) return false
        val o: PriceSimple = other as PriceSimple
        return (tool.name == o.tool.name)
    }

    override fun hashCode(): Int {
        return tool.name.hashCode()
    }
}

data class Candle(
    val priceHigh: Double = 0.0,
    val priceLow: Double = 0.0,
    val priceOpen: Double = 0.0,
    val priceClose: Double = 0.0,
    val time: Long = 0
) : Comparable<Candle> {
    override fun compareTo(other: Candle): Int {
        return time.compareTo(other.time)
    }

    override fun toString(): String {
        return "Candle: o=${priceOpen}; h=${priceHigh}; l=${priceLow}; c=${priceClose}; t=${time}"
            //"Candle: o=%1\$s; h=%2\$s; l=%3\$s; c=%4\$s; t=%5\$s",
    }
}

/**
 * from presentation
 */