package com.msgkatz.ratesapp.presentation.ui.chart.widget

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msgkatz.ratesapp.data.entities.rest.Asset
import com.msgkatz.ratesapp.domain.entities.Tool

class ChartParentToolPreviewParameterProvider: PreviewParameterProvider<List<Tool>> {
    override val values : Sequence<List<Tool>>
        get() = sequenceOf(
            listOf(
                Tool(
                    "BTCBNB",
                    Asset(0,"BTC", "Bitcoin"),
                    Asset(0,"BNB", "Binance Coin")
                ),
                Tool(
                    "BTCBNB2",
                    Asset(0,"BTC2", "Bitcoin2"),
                    Asset(0,"BNB2", "Binance Coin2"))
            )
        )
}