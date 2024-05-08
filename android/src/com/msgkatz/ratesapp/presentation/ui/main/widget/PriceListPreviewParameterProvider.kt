package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msgkatz.ratesapp.data.entities.rest.Asset
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.domain.entities.Tool

class PriceListPreviewParameterProvider: PreviewParameterProvider<List<PriceSimple>> {
    override val values : Sequence<List<PriceSimple>>
        get() = sequenceOf(
            listOf(
                PriceSimple(
                    Tool(
                        "BTCBNB",
                        Asset(0,"BTC", "Bitcoin"),
                        Asset(0,"BNB", "Binance Coin")),
                    23.105
                ),
                PriceSimple(
                    Tool(
                        "BTCBNB",
                        Asset(0,"BTC", "Bitcoin"),
                        Asset(0,"BNB", "Binance Coin")),
                    23.105
                ),
                PriceSimple(
                    Tool(
                        "BTCBNB",
                        Asset(0,"BTC", "Bitcoin"),
                        Asset(0,"BNB", "Binance Coin")),
                    23.105
                ),
            )
        )

}