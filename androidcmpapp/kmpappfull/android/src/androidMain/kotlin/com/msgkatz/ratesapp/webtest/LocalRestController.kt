package com.msgkatz.ratesapp.webtest

import com.msgkatz.ratesapp.data.network.rest.AssetApiModel
import com.msgkatz.ratesapp.data.network.rest.PlatformInfoDTApiModel
import com.msgkatz.ratesapp.data.network.rest.PriceByTickerApiModel
import com.msgkatz.ratesapp.data.network.rest.PriceSimpleDTApiModel
import com.msgkatz.ratesapp.data.network.rest.RestDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRestController(
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
): RestDataSource {
    private val defDispatcher: CoroutineDispatcher = Dispatchers.Default
    override suspend fun getPlatformInfo(): Result<PlatformInfoDTApiModel> =
        withContext(defDispatcher)
        {

            val res = getLocalPlatformInfo()
            //println("res getLocalPlatformInfo = : ${res.getOrNull()?.toolList?.size}")

            return@withContext res
        }


    override suspend fun getServerTime(): Result<Long> {
        TODO("Not yet implemented - getServerTime")
    }

    override suspend fun getPong(): Result<Int> {
        TODO("Not yet implemented - getPong")
    }

    override suspend fun getPriceByTicker(): Result<PriceByTickerApiModel> {
        TODO("Not yet implemented - getPriceByTicker")
    }

    override suspend fun getPriceSimple(): Result<List<PriceSimpleDTApiModel>> =
        withContext(defDispatcher)
        {
            getLocalPriceSimple()
        }


    override suspend fun getPriceByCandle(
        symbol: String?,
        interval: String?,
        startTime: Long?,
        endTime: Long?,
        limit: Int?
    ): Result<List<List<String>>> {
        TODO("Not yet implemented - getPriceByCandle")
    }

    override suspend fun getAssets(): Result<List<AssetApiModel>> {
        TODO("Not yet implemented - getAssets")
    }
}