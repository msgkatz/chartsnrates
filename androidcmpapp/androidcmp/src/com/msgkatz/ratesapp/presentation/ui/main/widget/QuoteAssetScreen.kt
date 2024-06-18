package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.placeholder
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.ui.app.InterimVMKeeper

@Composable
fun QuoteAssetRoute(
    quoteAssetName: String?,
    modifier: Modifier = Modifier,
    interimVMKeeper : InterimVMKeeper,
    onPriceItemClick: (PriceSimple) -> Unit,
    owner: ViewModelStoreOwner,

) {
    val viewModel: QuoteAssetViewModel = interimVMKeeper.makeQuoteAsset4(quoteAssetName, owner)
    val quoteAssetUiState by viewModel.quoteAssetUiState.collectAsState()
    val priceListUiState by viewModel.priceListUiState.collectAsState()

    QuoteAssetScreen(
        quoteAssetUIState = quoteAssetUiState,
        priceListUIState = priceListUiState,
        onPriceItemClick = onPriceItemClick,
        modifier = modifier,
    )

}

@Composable
fun QuoteAssetScreen(
    quoteAssetUIState: QuoteAssetUIState,
    priceListUIState: PriceListUIState,
    onPriceItemClick: (PriceSimple) -> Unit,
    modifier: Modifier = Modifier
) {

    // here we use LazyColumn that has build-in nested scroll, but we want to act like a
    // parent for this LazyColumn and participate in its nested scroll.
    // Let's make a collapsing header for LazyColumn
    val offsetDelta = remember { mutableStateOf(0f) }
    // now, let's create connection to the nested scroll system and listen to the scroll
    // happening inside child LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // try to consume before LazyColumn to collapse header if needed, hence pre-scroll
                val delta = available.y
                offsetDelta.value = delta
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return Offset.Zero
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuoteAssetHeader(
            quoteAssetUIState = quoteAssetUIState,
            scrollProvider = { offsetDelta.value }
        )
        QuoteAssetBody(
            priceListUIState = priceListUIState,
            onPriceItemClick = onPriceItemClick,
            nestedScrollConnection = nestedScrollConnection,
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
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
            priceListUIState = PriceListUIState.PriceList(priceSimpleList, placeholder(R.drawable.cur_bnb)),
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