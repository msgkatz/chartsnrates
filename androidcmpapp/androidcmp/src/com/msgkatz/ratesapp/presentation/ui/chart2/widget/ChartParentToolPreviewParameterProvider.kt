package com.msgkatz.ratesapp.presentation.ui.chart2.widget

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msgkatz.ratesapp.data.entities.rest.AssetDT
import com.msgkatz.ratesapp.domain.entities.ToolJava

class ChartParentToolPreviewParameterProvider: PreviewParameterProvider<List<ToolJava>> {
    override val values : Sequence<List<ToolJava>>
        get() = sequenceOf(
            listOf(
                ToolJava(
                    "BTCBNB",
                    AssetDT(
                        0,
                        "BTC",
                        "Bitcoin"
                    ),
                    AssetDT(
                        0,
                        "BNB",
                        "Binance Coin"
                    )
                ),
                ToolJava(
                    "BTCBNB2",
                    AssetDT(
                        0,
                        "BTC2",
                        "Bitcoin2"
                    ),
                    AssetDT(
                        0,
                        "BNB2",
                        "Binance Coin2"
                    )
                )
            )
        )
}