package com.msgkatz.ratesapp.feature.chartgdx.widget

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msgkatz.ratesapp.old.data.entities.rest.AssetDT
import com.msgkatz.ratesapp.old.domain.entities.ToolJava

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