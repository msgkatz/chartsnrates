package com.msgkatz.ratesapp.presentation.ui.quoteasset2

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PriceSimple
import com.msgkatz.ratesapp.data.model.Tool


class PriceListPreviewParameterProvider: PreviewParameterProvider<List<PriceSimple>> {
    override val values : Sequence<List<PriceSimple>>
        get() = sequenceOf(
            listOf(
                PriceSimple(
                    tool = Tool(
                        name = "BTCBNB",
                        baseAsset = Asset(0, "BTC", "Bitcoin"),
                        quoteAsset = Asset(0, "BNB", "Binance Coin"),
                        isActive = true
                    ),
                    23.105
                ),
                PriceSimple(
                    tool = Tool(
                        name = "BTCBNB2",
                        baseAsset = Asset(0, "BTC2", "Bitcoin2"),
                        quoteAsset = Asset(0, "BNB2", "Binance Coin2"),
                        isActive = true
                    ),
                    23.105
                ),
                PriceSimple(
                    tool = Tool(
                        name = "BTCBNB3",
                        baseAsset = Asset(0, "BTC3", "Bitcoin3"),
                        quoteAsset = Asset(0, "BNB3", "Binance Coin3"),
                        isActive = true
                    ),
                    23.105
                ),

            )
        )

}