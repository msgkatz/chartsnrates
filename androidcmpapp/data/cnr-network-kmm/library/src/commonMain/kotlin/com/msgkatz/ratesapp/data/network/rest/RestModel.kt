package com.msgkatz.ratesapp.data.network.rest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val BASE_URL = "https://www.binance.com/"
const val BASE_URL_TO_CHANGE = "ex.bnbstatic.com/images"
const val BASE_URL_TO_CHANGE_TO = "www.binance.com/file/resources/img"

/**
 * Assets are BTC, ETH, etc.
 */
@Serializable
data class AssetApiModel(
    @SerialName("id") val id: Int = 0,
    @SerialName("assetCode") val nameShort: String? = null,
    @SerialName("assetName") val nameLong: String? = null,
    @SerialName("logoUrl") val logoShortUrl: String? = null,
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
data class ToolDTApiModel(
    @SerialName("symbol") val symbol: String? = null,
    @SerialName("baseAsset") val baseAsset: String? = null,
    @SerialName("baseAssetPrecision") val basePrecision: Int = 0,
    @SerialName("quoteAsset") val quoteAsset: String? = null,
    @SerialName("quotePrecision") val quotePrecision: Int = 0,
    @SerialName("status") val status: String? = null
)

@Serializable
data class ServerTimeApiModel(
    @SerialName("serverTime") val serverTime: Long = 0
)

@Serializable
data class PriceSimpleDTApiModel(
    @SerialName("symbol") val instrumentSymbol: String? = null,
    @SerialName("price") val price: Double = 0.0
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
data class PriceByTickerApiModel(
    @SerialName("symbol") val symbol: String? = null,
    @SerialName("prevClosePrice") val prevClosePrice: Double = 0.0,
    @SerialName("lastPrice") val lastPrice: Double = 0.0,
    @SerialName("openPrice") val openPrice: Double = 0.0,
    @SerialName("highPrice") val highPrice: Double = 0.0,
    @SerialName("lowPrice") val lowPrice: Double = 0.0,
    @SerialName("openTime") val openTime: Long = 0,
    @SerialName("closeTime") val closeTime: Long = 0
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
data class PriceByCandleApiModel (
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
data class PlatformInfoDTApiModel(
    @SerialName("timezone") val timeZone: String? = null,
    @SerialName("serverTime") val serverTime: Long = 0,
    @SerialName("symbols") val toolList: List<ToolDTApiModel>? = null
)