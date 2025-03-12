package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Candle
import kotlinx.coroutines.flow.Flow

interface CurToolRealtimeBalancedPriceRepository {
    fun subscribeToolPrice(
        symbol: String,
        interval: String
    ): Flow<Candle>

    suspend fun getInterimPrices(
        symbol: String,
        interval: String,
        startTime: Long,
        endTime: Long?,
    ): List<Candle>
}