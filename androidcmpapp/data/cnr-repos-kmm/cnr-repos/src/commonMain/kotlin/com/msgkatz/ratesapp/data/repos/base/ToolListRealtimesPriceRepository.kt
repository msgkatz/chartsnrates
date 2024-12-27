package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.PriceSimple
import kotlinx.coroutines.flow.Flow

interface ToolListRealtimesPriceRepository {

    fun subscribeToolPrices(): Flow<Map<String, Set<PriceSimple>>>
}