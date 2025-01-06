package com.msgkatz.ratesapp.data.network.websocket

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.json.Json

/**
 * WSApiImpl
 */
class WebSocketController constructor(
    private val coroutineScope: CoroutineScope,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
    private val debug: Boolean = false,
) : WebSocketDataSource {

    private val client: WebSocketClient

    init {
        client = WebSocketClient(coroutineScope, coroutineDispatcher, debug)
    }

/**
    override fun getKlineStream(symbol: String, interval: String): Flow<String> {
        val path = "${BASE_URL_WSOCK}${BASE_URL_WSOCK_RAW}${symbol}@kline_${interval}"
        return client.connect(path)
    }
 **/
    override fun getKlineStream(symbol: String, interval: String): SharedFlow<StreamKlineEventWSModel> {
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_RAW${symbol}@kline_${interval}"
        val clientTyped = WebSocketClientTyped<StreamKlineEventWSModel>(coroutineScope, coroutineDispatcher, debug)
        return clientTyped.connectTyped(path) { it ->
            try {
                val withUnknownKeys = Json { ignoreUnknownKeys = true }
                withUnknownKeys.decodeFromString<StreamKlineEventWSModel>(it)
            } catch (e: Exception) {
                null
            }
        }
    }


    override fun getMiniTickerStream(symbol: String): SharedFlow<String> {
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_RAW${symbol}@miniTicker"
        return client.connect(path)
    }

    /**
    override fun getMiniTickerStreamAll(): Flow<String> {
    val path = "${BASE_URL_WSOCK}${BASE_URL_WSOCK_RAW}!miniTicker@arr"
    return client.connect(path)
    }
     **/
    override fun getMiniTickerStreamAll(): SharedFlow<List<StreamMarketTickerMiniWSModel>> {
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_RAW!miniTicker@arr"
        val clientTyped = WebSocketClientTyped<List<StreamMarketTickerMiniWSModel>>(coroutineScope, coroutineDispatcher, debug)
        return clientTyped.connectTyped(path) { it ->
            try {
                val withUnknownKeys = Json { ignoreUnknownKeys = true }
                withUnknownKeys.decodeFromString<List<StreamMarketTickerMiniWSModel>>(it)
            } catch (e: Exception) {
                println(e.message)
                null
            }
        }
    }

    override fun getTickerStream(symbol: String): SharedFlow<String> {
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_RAW${symbol}@ticker"
        return client.connect(path)
    }

    override fun getTickerStreamAll(): SharedFlow<String> {
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_RAW!ticker@arr"
        return client.connect(path)
    }

    /**
    override fun getKlineAndMiniTickerComboStream(symbol: String, interval: String): Flow<String> {
    val path = "${BASE_URL_WSOCK}${BASE_URL_WSOCK_COMBINED}${symbol}@kline_${interval}/${symbol}@miniTicker"
    return client.connect(path)
    }
     **/
    override fun getKlineAndMiniTickerComboStream(
        symbol: String,
        interval: String
    ): SharedFlow<StreamComboBaseWSModel> {
        val path = "$BASE_URL_WSOCK$BASE_URL_WSOCK_COMBINED${symbol}@kline_${interval}/${symbol}@miniTicker"
        val clientTyped = WebSocketClientTyped<StreamComboBaseWSModel>(coroutineScope, coroutineDispatcher, debug)
        return clientTyped.connectTyped(path) { it ->
            try {
                val withUnknownKeys = Json { ignoreUnknownKeys = true }
                withUnknownKeys.decodeFromString<StreamComboBaseWSModel>(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun getKlineAndMiniTickerComboStreamString(
        symbol: String,
        interval: String
    ): SharedFlow<String> {
        val path = "${BASE_URL_WSOCK}${BASE_URL_WSOCK_COMBINED}${symbol}@kline_${interval}/${symbol}@miniTicker"
        println("path = ${path}")
        return client.connect(path)
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