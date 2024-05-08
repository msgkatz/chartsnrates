package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter

@Composable
fun QuoteAssetScreen(
    quoteAssetUIState: QuoteAssetUIState,
    priceListUIState: PriceListUIState,
    onPriceItemClick: (PriceSimple) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuoteAssetHeader(
            quoteAssetUIState = quoteAssetUIState
        )
        QuoteAssetBody(
            priceListUIState = priceListUIState,
            onPriceItemClick = onPriceItemClick
        )
    }
}

@Preview
@Composable
fun QuoteAssetScreenWithDataPreview(
    @PreviewParameter(PriceListPreviewParameterProvider::class)
    priceSimpleList: List<PriceSimple>,
){
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {
        QuoteAssetScreen(
            quoteAssetUIState = QuoteAssetUIState.Data(priceSimpleList.get(0).tool.baseAsset),
            priceListUIState = PriceListUIState.PriceList(priceSimpleList),
            onPriceItemClick = {}
        )
    }
}

@Preview
@Composable
fun QuoteAssetScreenLoadingPreview(){
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {
        QuoteAssetScreen(
            quoteAssetUIState = QuoteAssetUIState.Loading,
            priceListUIState = PriceListUIState.Loading,
            onPriceItemClick = {}
        )
    }
}

@Preview
@Composable
fun QuoteAssetScreenEmptyPreview(){
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {
        QuoteAssetScreen(
            quoteAssetUIState = QuoteAssetUIState.Empty,
            priceListUIState = PriceListUIState.Empty,
            onPriceItemClick = {}
        )
    }
}