package com.msgkatz.ratesapp.feature.quoteasset

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModelStoreOwner

@Immutable
interface QuoteAssetKeeper {
    fun makeQuoteAsset5(quoteAssetName: String?, _owner: ViewModelStoreOwner): QuoteAssetViewModel
}