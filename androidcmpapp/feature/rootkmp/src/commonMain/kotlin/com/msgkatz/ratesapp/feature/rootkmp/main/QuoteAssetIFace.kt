package com.msgkatz.ratesapp.feature.rootkmp.main

import kotlinx.coroutines.flow.StateFlow

interface QuoteAssetIFace {

    val quoteAssetUiState: StateFlow<QuoteAssetUIState>
    val priceListUiState: StateFlow<PriceListUIState>
}