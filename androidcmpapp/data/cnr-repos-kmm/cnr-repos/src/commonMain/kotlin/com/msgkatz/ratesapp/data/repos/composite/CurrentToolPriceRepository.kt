package com.msgkatz.ratesapp.data.repos.composite

import com.msgkatz.ratesapp.data.model.Candle
import kotlinx.coroutines.flow.Flow

interface CurrentToolPriceRepository {
    suspend fun getPriceHistoryByTool(symbol: String,
                        interval: String,
                        startTime: Long,
                        endTime: Long,
                        limit: Int
    ): List<Candle>?

    suspend fun getPricesInterimByTool(symbol: String,
                                      interval: String,
                                      startTime: Long
    ): List<Candle>?

    suspend fun getToolRealtimePrice(symbol: String,
                                       interval: String,
    ): Flow<Candle>

    suspend fun getToolRealtimePriceCombo(symbol: String,
                                       interval: String
    ): Flow<Candle?>
}