package com.msgkatz.ratesapp.data.network.rest

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.utils.io.core.use
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * RestApiImpl
 */
class RestController constructor(
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
    //private val client: HttpClient = getRestClient()
) : RestDataSource {
    private val client: HttpClient
        get() = getRestClient()

    override suspend fun getPlatformInfo(): Result<PlatformInfoDTApiModel> = withContext(ioDispatcher) {
        runCatching {
            client.use { // for single request use 'use' it automatically release the resources and calls close
                it.get { // get method
                    url {
                        protocol = URLProtocol.HTTPS
                        host = BASE_URL
                        path("$BASE_PATH_REST/v1/exchangeInfo")
                    }
                }
            }.body()
        }
    }

    override suspend fun getServerTime(): Result<Long> = withContext(ioDispatcher) {
        runCatching {
            client.use { // for single request use 'use' it automatically release the resources and calls close
                it.get { // get method
                    url {
                        protocol = URLProtocol.HTTPS
                        host = BASE_URL
                        path("$BASE_PATH_REST/v1/time")
                    }
                }
            }.body()
        }
    }

    override suspend fun getPong(): Result<Int> = withContext(ioDispatcher) {
        runCatching {
            client.use { // for single request use 'use' it automatically release the resources and calls close
                it.get { // get method
                    url {
                        protocol = URLProtocol.HTTPS
                        host = BASE_URL
                        path("$BASE_PATH_REST/v1/ping")
                    }
                }
            }.body()
        }
    }

    override suspend fun getPriceByTicker(): Result<PriceByTickerApiModel> = withContext(ioDispatcher) {
        runCatching {
            client.use { // for single request use 'use' it automatically release the resources and calls close
                it.get { // get method
                    url {
                        protocol = URLProtocol.HTTPS
                        host = BASE_URL
                        path("$BASE_PATH_REST/v1/ticker/24hr")
                    }
                }
            }.body()
        }
    }

    override suspend fun getPriceSimple(): Result<List<PriceSimpleDTApiModel>> = withContext(ioDispatcher) {
        runCatching {
            client.use { // for single request use 'use' it automatically release the resources and calls close
                it.get { // get method
                    url {
                        protocol = URLProtocol.HTTPS
                        host = BASE_URL
                        path("$BASE_PATH_REST/v3/ticker/price")
                    }
                }
            }.body()
        }
    }

    override suspend fun getPriceByCandle(
        symbol: String?,
        interval: String?,
        startTime: Long?,
        endTime: Long?,
        limit: Int?
    ): Result<List<List<String>>> = withContext(ioDispatcher) {
        runCatching {
            client.use { сit -> // for single request use 'use' it automatically release the resources and calls close
                сit.get { // get method
                    url {
                        protocol = URLProtocol.HTTPS
                        host = BASE_URL
                        path("$BASE_PATH_REST/v1/klines")
                        symbol?.let { parameters.append("symbol", it) }
                        interval?.let { parameters.append("interval", it) }
                        startTime?.let { parameters.append("startTime", it.toString()) }
                        endTime?.let { parameters.append("endTime", it.toString()) }
                        limit?.let { parameters.append("limit", it.toString()) }
                    }
                }
            }.body()
        }
    }

    override suspend fun getAssets(): Result<List<AssetApiModel>> = withContext(ioDispatcher) {
        runCatching {
            client.use { // for single request use 'use' it automatically release the resources and calls close
                it.get { // get method
                    url {
                        protocol = URLProtocol.HTTPS
                        host = BASE_URL
                        path("$BASE_PATH_REST/assetWithdraw/getAllAsset.html")
                    }
                }
            }.body<List<AssetApiModel>>()
        }
    }

    companion object {
        //const val BASE_URL: String = "https://www.binance.com/"
        const val BASE_URL: String = "www.binance.com"
        const val BASE_URL_REST: String = "https://www.binance.com/api/"
        const val BASE_PATH_REST: String = "api";
    }
}