package com.msgkatz.ratesapp.presentation.ui.quoteasset

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msgkatz.ratesapp.data.entities.rest.AssetDT
import com.msgkatz.ratesapp.domain.entities.PriceSimpleJava
import com.msgkatz.ratesapp.domain.entities.ToolJava

class PriceListPreviewParameterProvider: PreviewParameterProvider<List<PriceSimpleJava>> {
    override val values : Sequence<List<PriceSimpleJava>>
        get() = sequenceOf(
            listOf(
                PriceSimpleJava(
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
                    23.105
                ),
                PriceSimpleJava(
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
                    ),
                    23.105
                ),
                PriceSimpleJava(
                    ToolJava(
                        "BTCBNB3",
                        AssetDT(
                            0,
                            "BTC3",
                            "Bitcoin3"
                        ),
                        AssetDT(
                            0,
                            "BNB3",
                            "Binance Coin3"
                        )
                    ),
                    23.105
                ),
            )
        )

}