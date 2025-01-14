package com.msgkatz.ratesapp.presentation.ui.quoteasset2

import androidx.lifecycle.ViewModelStoreOwner

interface QuoteAssetKeeper {
    fun makeQuoteAsset5(quoteAssetName: String?, _owner: ViewModelStoreOwner): QuoteAssetViewModel
}