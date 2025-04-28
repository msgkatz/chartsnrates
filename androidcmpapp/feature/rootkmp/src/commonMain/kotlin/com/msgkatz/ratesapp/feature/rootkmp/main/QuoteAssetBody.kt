package com.msgkatz.ratesapp.feature.rootkmp.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.data.model.PriceSimple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



//@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun QuoteAssetBodyFlowed(
    priceListUIState: PriceListUIState,
    onPriceItemClick: (PriceSimple) -> Unit,
    scrollableState: LazyListState,
    nestedScrollConnection: NestedScrollConnection,
    withBottomSpacer: Boolean = true,
    modifier: Modifier = Modifier
) {
    //val scrollableState = rememberLazyListState()
    val nestedScrollDispatcher = remember {
        NestedScrollDispatcher()
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            // attach as a parent to the nested scroll system
            .nestedScroll(
                connection = nestedScrollConnection,
                dispatcher = nestedScrollDispatcher
            ),
    ) {

        when (priceListUIState) {
            is PriceListUIState.Loading -> {}
            is PriceListUIState.Empty -> {}
            is PriceListUIState.PriceList -> {}
            is PriceListUIState.PriceListFlow -> {

                val priceListState = remember { mutableStateListOf<PriceSimpleUI>() }
                LaunchedEffect(Unit) {
                    priceListUIState.data.flow
                        //.flowOn(Dispatchers.Main)
                        .collect { list ->
                            println("QuoteAssetBodyFlowed :: ::${list?.size ?: 0}")
                            list.forEach { _price ->
                                val price = _price.toUI()
                                val idx = priceListState.indexOfFirst { it.tool.name == price.tool.name }
                                withContext(Dispatchers.Main) {
                                    if (idx != -1 && price.price != priceListState[idx].price) {
                                        priceListState[idx] = priceListState[idx].copy(
                                            price = price.price,
                                            pricePrev = priceListState[idx].price
                                        )
                                    } else if (idx == -1) {
                                        priceListState.add(price)
                                    }
                                }
                            }

                        }
                }

                LazyColumn(
                    modifier = Modifier
                        //.padding(horizontal = 24.dp)
                    //.testTag("quoteasset:body")
                    ,
                    contentPadding = PaddingValues(
                        top = headerHeight, //140.dp,
                        start = 0.dp,
                        end = 0.dp
                    ),
                    state = scrollableState,
                ) {
                    items (
                        items = priceListState,
                        key = { price -> price.tool.name }
                    ) { it ->
                        PriceListItemFlowed(
                            priceSimple = it,
                            imageUrl = it.tool.baseAsset.getLogoFullUrlM() ?: "",
                            onItemClick = { onPriceItemClick(it.toPriceSimple()) },
                            priceListUIState = priceListUIState,
                        )

                    }

                    if (withBottomSpacer) {
                        item {
                            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                        }
                    }

                }
            }
        }
    }
}