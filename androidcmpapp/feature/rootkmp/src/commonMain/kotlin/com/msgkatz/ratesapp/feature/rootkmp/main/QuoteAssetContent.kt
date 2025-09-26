package com.msgkatz.ratesapp.feature.rootkmp.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import com.msgkatz.ratesapp.data.model.PriceSimple

@Composable
fun QuoteAssetContent(
    modifier: Modifier = Modifier,
    component: QuoteAssetIFace,
    onPriceItemClick: (PriceSimple) -> Unit,
) {
    val quoteAssetUiState by component.quoteAssetUiState.collectAsState()
    val priceListUiState by component.priceListUiState.collectAsState()

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
    val scrollableState = rememberLazyListState()
    val derivedScroll by remember {
        derivedStateOf { scrollableState.firstVisibleItemIndex == 0 }
    }


    LaunchedEffect(true) {
        snapshotFlow { scrollableState.firstVisibleItemIndex to scrollableState.firstVisibleItemScrollOffset }
            .collect { it ->
                //println("${it.first} -- ${it.second}")

            }
    }

    // here we use LazyColumn that has build-in nested scroll, but we want to act like a
    // parent for this LazyColumn and participate in its nested scroll.
    // Let's make a collapsing header for LazyColumn
    val offsetDelta = rememberSaveable { mutableStateOf(0f) }
    // now, let's create connection to the nested scroll system and listen to the scroll
    // happening inside child LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                // try to consume before LazyColumn to collapse header if needed, hence pre-scroll
                val delta = if (available.y < 0) {
                    available.y
                } else {
                    if (derivedScroll) available.y else 0
                }
                offsetDelta.value = delta.toFloat()
                //println("delta :: $delta")
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return Offset.Zero
            }


        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        QuoteAssetHeader(
            quoteAssetUIState = quoteAssetUIState,
            scrollProvider = { offsetDelta.value },
            //scrollProvider = { 0f },
        )
        QuoteAssetBodyFlowed(
            //QuoteAssetBody(
            priceListUIState = priceListUIState,
            onPriceItemClick = onPriceItemClick,
            scrollableState = scrollableState,
            nestedScrollConnection = nestedScrollConnection,
        )
    }
}

////@OptIn(ExperimentalGlideComposeApi::class)
//@Preview
//@Composable
//fun QuoteAssetScreenWithDataPreview(
//    @PreviewParameter(PriceListPreviewParameterProvider::class)
//    priceSimpleList: List<PriceSimple>,
//){
//
//    QuoteAssetScreen(
//        quoteAssetUIState = QuoteAssetUIState.Data(priceSimpleList.get(0).tool.baseAsset),
//        priceListUIState = PriceListUIState.PriceList(priceList = priceSimpleList, placeHolder = "R.drawable.cur_bnb"),
//        onPriceItemClick = {}
//    )
//
//}
//
//@Preview
//@Composable
//fun QuoteAssetScreenLoadingPreview(){
//
//    QuoteAssetScreen(
//        quoteAssetUIState = QuoteAssetUIState.Loading,
//        priceListUIState = PriceListUIState.Loading,
//        onPriceItemClick = {}
//    )
//
//}
//
//@Preview
//@Composable
//fun QuoteAssetScreenEmptyPreview(){
//    QuoteAssetScreen(
//        quoteAssetUIState = QuoteAssetUIState.Empty,
//        priceListUIState = PriceListUIState.Empty,
//        onPriceItemClick = {}
//    )
//
//}