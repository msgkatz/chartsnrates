package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Candle
import kotlinx.coroutines.flow.Flow

interface HistoricalPriceRepository {

    suspend fun getData(symbol: String,
                        interval: String,
                        startTime: Long,
                        endTime: Long,
                        limit: Int
    ): List<Candle>?

    fun getDataAsFlow(symbol: String,
                        interval: String,
                        startTime: Long,
                        endTime: Long,
                        limit: Int
    ): Flow<List<Candle>?>
}