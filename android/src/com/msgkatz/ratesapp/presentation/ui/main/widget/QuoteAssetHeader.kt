package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.data.entities.rest.Asset
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import java.util.Locale

@Composable
fun QuoteAssetHeader(
    quoteAssetUIState: QuoteAssetUIState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (quoteAssetUIState) {
            is QuoteAssetUIState.Data -> {
                Text(text = makeMainTitleText(quoteAssetUIState.quoteAsset))
                Text(text = quoteAssetUIState.quoteAsset.nameLong)

            }
            is QuoteAssetUIState.Loading -> {
                Text(text = "")
                Text(text = "")
            }
            is QuoteAssetUIState.Empty -> {
                Text(text = "")
                Text(text = "")
            }
        }


        Spacer(Modifier
            .height(18.dp)
            .windowInsetsTopHeight(WindowInsets.safeDrawing)
        )
        GradientBackground()
        Spacer(Modifier
            .height(18.dp)
            .windowInsetsTopHeight(WindowInsets.safeDrawing)
        )
        Text(text = stringResource(id = R.string.screen_one_your_stocks))

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
                horizontal = 70.dp,
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
            quoteAssetUIState = QuoteAssetUIState.Data(Asset(0,"BTC", "Bitcoin"))
        )
    }
}

@Composable
private fun makeMainTitleText(quoteAset: Asset): String {
    return String.format(
        Locale.getDefault(), "%1\$s %2\$s",
        quoteAset.getNameShort(), stringResource(R.string.screen_one_markets)
    )
}