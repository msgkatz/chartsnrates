package com.msgkatz.ratesapp.data.network.websocket

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class WebSocketController3rd constructor(
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val debug: Boolean = false,
) : WebSocketDataSource {

    private val withUnknownKeys = Json { ignoreUnknownKeys = true }
    private var client: WebSocketClient3rd? = null

    private inline fun <reified T> transform(message: String?): T? {
        message?.let {
            val ret = try { withUnknownKeys.decodeFromString<T>(it) } catch (e: Exception) { null }
            return ret
        }
        return null
    }

    private fun renewClient(): WebSocketClient3rd {
        client?.cancel()
        return WebSocketClient3rd(coroutineScope, ioDispatcher, debug)
    }

    override fun getKlineStream(symbol: String, interval: String): Flow<StreamKlineEventWSModel> {
        client = renewClient()
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_RAW${symbol}@kline_${interval}"
        return client!!.connect(path).map { it -> transform<StreamKlineEventWSModel>(it) }.filterNotNull()
    }

    override fun getMiniTickerStream(symbol: String): Flow<String> {
        client = renewClient()
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_RAW${symbol}@miniTicker"
        return client!!.connect(path)
    }

    override fun getMiniTickerStreamAll(): Flow<List<StreamMarketTickerMiniWSModel>> {
        client = renewClient()
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_RAW!miniTicker@arr"
        return client!!.connect(path).map { it -> transform<List<StreamMarketTickerMiniWSModel>>(it) }.filterNotNull()
    }

    override fun getTickerStream(symbol: String): Flow<String> {
        client = renewClient()
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_RAW${symbol}@ticker"
        return client!!.connect(path)
    }

    override fun getTickerStreamAll(): Flow<String> {
        client = renewClient()
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_RAW!ticker@arr"
        return client!!.connect(path)
    }

    override fun getKlineAndMiniTickerComboStream(
        symbol: String,
        interval: String
    ): Flow<StreamComboBaseWSModel> {
        client = renewClient()
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_COMBINED${symbol}@kline_${interval}/${symbol}@miniTicker"
        return client!!.connect(path).map { it -> transform<StreamComboBaseWSModel>(it) }.filterNotNull()
    }

    override fun getKlineAndMiniTickerComboStreamString(
        symbol: String,
        interval: String
    ): Flow<String> {
        client = renewClient()
        val path = "${BASE_URL_WSOCK}${BASE_URL_WSOCK_COMBINED}${symbol}@kline_${interval}/${symbol}@miniTicker"
        return client!!.connect(path)
    }

    companion object {
        const val BASE_URL: String = "https://www.binance.com/"
        const val BASE_URL_REST: String = "https://www.binance.com/api/"
        const val BASE_URL_WSOCK: String = "wss://stream.binance.com:9443"
        const val BASE_URL_WSOCK_RAW: String = "/ws/"
        const val BASE_URL_WSOCK_COMBINED: String = "/stream?streams="
        /**
         * <string name="ws_kline">%1$s\@kline_%2$s</string>
         *     <string name="ws_mini_ticker">%1$s\@miniTicker</string>
         *     <string name="ws_mini_ticker_all">!miniTicker\@arr</string>
         *     <string name="ws_ticker">%1$s\@ticker</string>
         *     <string name="ws_ticker_all">!ticker\@arr</string>
         */
    }

}