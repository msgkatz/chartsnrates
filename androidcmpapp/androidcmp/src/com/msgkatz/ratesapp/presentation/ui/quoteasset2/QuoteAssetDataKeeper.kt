package com.msgkatz.ratesapp.presentation.ui.quoteasset2

import android.graphics.drawable.Drawable
import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PriceSimple
import kotlinx.coroutines.flow.Flow

interface QuoteAssetDataKeeper {

    //tmpDataKeeper.toolRepository.getQuoteAssetMap()
    suspend fun getQuoteAssetMap() : Map<String, Asset>?

    //tmpDataKeeper.toolListPriceRepository.getToolPrices()),
    suspend fun getToolPrices(): Map<String, Set<PriceSimple>>

    //tmpDataKeeper.toolListRealtimesPriceRepository.subscribeToolPrices()
    fun subscribeToolPrices(): Flow<Map<String, Set<PriceSimple>>>
}

interface QuoteAssetDrawableDataKeeper {
    fun getSmallDrawableByQuoteAssetName(name: String?): Drawable
}