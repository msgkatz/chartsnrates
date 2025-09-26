package com.msgkatz.ratesapp.feature.rootkmp.main

import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PriceSimple
import kotlinx.coroutines.flow.Flow

interface QuoteAssetDataKeeper {
    suspend fun getQuoteAssetMap() : Map<String, Asset>?
    suspend fun getToolPrices(): Map<String, Set<PriceSimple>>
    fun subscribeToolPrices(): Flow<Map<String, Set<PriceSimple>>>
}