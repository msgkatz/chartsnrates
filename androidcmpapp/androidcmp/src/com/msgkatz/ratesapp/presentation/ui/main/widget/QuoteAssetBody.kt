package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.msgkatz.ratesapp.domain.entities.PriceSimple

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun QuoteAssetBody(
    priceListUIState: PriceListUIState,
    onPriceItemClick: (PriceSimple) -> Unit,
    withBottomSpacer: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        val scrollableState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                //.testTag("quoteasset:body")
            ,
            contentPadding = PaddingValues(vertical = 16.dp),
            state = scrollableState,
        ) {
            when (priceListUIState) {
                is PriceListUIState.Loading -> {}
                is PriceListUIState.Empty -> {}
                is PriceListUIState.PriceList -> {
                    priceListUIState.priceList.forEach {
                        val id = it.tool.name
                        item(key = id) { 
                            PriceListItem(
                                priceSimple = it,
                                imageUrl = it.getTool().getBaseAsset().getLogoFullUrl(),
                                onItemClick = { onPriceItemClick(it) },
                                iconPlaceholder = priceListUIState.placeHolder
                            )
                        }
                    }
                }
            }

            if (withBottomSpacer) {
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
            
        }
    }
}