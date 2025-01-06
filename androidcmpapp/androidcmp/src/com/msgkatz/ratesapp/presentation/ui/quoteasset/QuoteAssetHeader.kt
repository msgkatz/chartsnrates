package com.msgkatz.ratesapp.presentation.ui.quoteasset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.data.entities.rest.AssetDT
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import java.util.Locale


private val headerHeight = 128.dp

@Composable
fun QuoteAssetHeader(
    quoteAssetUIState: QuoteAssetUIState,
    scrollProvider: () -> Float,
    modifier: Modifier = Modifier,
) {

    val headerHeightPx = with(LocalDensity.current) { headerHeight.roundToPx().toFloat() }
    val headerOffsetHeightPx = remember { mutableStateOf(0f) }

    Column(
        modifier = modifier.padding(horizontal = 33.dp)
            //.heightIn(max = headerHeight)

            .height(headerHeight)
            .graphicsLayer {
                val scroll = scrollProvider()
                val newOffset = headerOffsetHeightPx.value + scroll
                headerOffsetHeightPx.value = newOffset.coerceIn(-headerHeightPx, 0f)
                translationY = headerOffsetHeightPx.value
            }

//            .offset {
//                val scroll = scrollProvider()
//                val newOffset = headerOffsetHeightPx.value + scroll
//                headerOffsetHeightPx.value = newOffset.coerceIn(-headerHeightPx, 0f)
//                IntOffset(x = 0, y = headerOffsetHeightPx.value.roundToInt())
//            }
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(
            Modifier
                .height(15.dp)
                .windowInsetsTopHeight(WindowInsets.safeDrawing)
        )
        when (quoteAssetUIState) {
            is QuoteAssetUIState.Data -> {
                Text(
                    text = makeMainTitleText(quoteAssetUIState.quoteAsset),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )
                Spacer(
                    Modifier
                        .height(5.dp)
                        .windowInsetsTopHeight(WindowInsets.safeDrawing)
                )
                Text(
                    text = quoteAssetUIState.quoteAsset.nameLong,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }
            is QuoteAssetUIState.Loading -> {
                Text(text = "")
                Spacer(
                    Modifier
                        .height(10.dp)
                        .windowInsetsTopHeight(WindowInsets.safeDrawing)
                )
                Text(text = "")
            }
            is QuoteAssetUIState.Empty -> {
                Text(text = "")
                Spacer(
                    Modifier
                        .height(10.dp)
                        .windowInsetsTopHeight(WindowInsets.safeDrawing)
                )
                Text(text = "")
            }
        }

        Spacer(
            Modifier
                .height(18.dp)
                .windowInsetsTopHeight(WindowInsets.safeDrawing)
        )
        GradientBackground(modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(
            Modifier
                .height(18.dp)
                .windowInsetsTopHeight(WindowInsets.safeDrawing)
        )
        Text(
            text = stringResource(id = R.string.screen_one_your_stocks),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(
            Modifier
                .height(10.dp)
                .windowInsetsTopHeight(WindowInsets.safeDrawing)
        )

    }
}

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(
                horizontal = 40.dp,
                vertical = 0.dp
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0x1AFFFFFF),
                        Color(0xFFE0E9D1),
                        Color(0x1AFFFFFF)
                    )
                )
            )
    )
}

@Preview
@Composable
fun QuoteAssetHeaderPreview() {
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {
        QuoteAssetHeader(
            quoteAssetUIState = QuoteAssetUIState.Data(
                AssetDT(
                    0,
                    "BTC",
                    "Bitcoin"
                )
            ),
            scrollProvider = { 0f },
        )
    }
}

@Composable
private fun makeMainTitleText(quoteAset: AssetDT): String {
    return String.format(
        Locale.getDefault(), "%1\$s %2\$s",
        quoteAset.getNameShort(), stringResource(R.string.screen_one_markets)
    )
}