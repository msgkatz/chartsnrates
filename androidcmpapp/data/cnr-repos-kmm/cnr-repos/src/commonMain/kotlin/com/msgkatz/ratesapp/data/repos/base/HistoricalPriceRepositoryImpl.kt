package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Candle
import com.msgkatz.ratesapp.data.network.rest.RestDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class HistoricalPriceRepositoryImpl(
    private val networkds: RestDataSource,
    private val map: MutableMap<String, MutableSet<Candle>>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : HistoricalPriceRepository {

    private var curset: MutableSet<Candle>? = null //LinkedHashSet()

    suspend fun getData(symbol: String?,
                        interval: String?,
                        startTime: Long?,
                        endTime: Long?,
                        limit: Int?
    ) {
        networkds.getPriceByCandle(symbol, interval, startTime, endTime, limit)
    }

}