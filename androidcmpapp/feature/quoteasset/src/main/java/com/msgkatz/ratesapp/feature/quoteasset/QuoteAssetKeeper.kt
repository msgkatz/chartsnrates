package com.msgkatz.ratesapp.feature.quoteasset

import androidx.lifecycle.ViewModelStoreOwner

interface QuoteAssetKeeper {
    fun makeQuoteAsset5(quoteAssetName: String?, _owner: ViewModelStoreOwner): QuoteAssetViewModel
}