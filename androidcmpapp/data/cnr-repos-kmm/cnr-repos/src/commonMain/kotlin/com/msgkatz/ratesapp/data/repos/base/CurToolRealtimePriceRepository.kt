package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Candle
import kotlinx.coroutines.flow.Flow

interface CurToolRealtimePriceRepository {
    fun subscribeToolPriceCombo(symbol: String, interval: String): Flow<Candle?>
}