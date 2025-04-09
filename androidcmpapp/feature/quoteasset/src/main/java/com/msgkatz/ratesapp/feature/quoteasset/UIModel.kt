package com.msgkatz.ratesapp.feature.quoteasset

import androidx.compose.runtime.Immutable
import com.msgkatz.ratesapp.data.model.PriceSimple
import com.msgkatz.ratesapp.data.model.Tool


fun PriceSimple.toUI() = PriceSimpleUI(
    tool = this.tool.copy(),
    price = this.price,
    pricePrev = this.pricePrev
)

/**
 * Need to separate models for PriceSimple and PriceSimpleUI - one needs equals while other not:
 *
 * 1) equality was affecting mutableStateListOf<PriceSimple> inside compose
 * so stateList values unable to update their price
 * and equality using only toolName was disabled
 *
 * 2) but equality was needed by collection structures to not duplicate incoming items,
 * so we separated models and made two - PriceSimple and PriceSimpleUI - one needs equals while other don't
 */

@Immutable
data class PriceSimpleUI(
    val tool: Tool,
    val price: Double,
    val pricePrev: Double = 0.0
) : Comparable<PriceSimpleUI> {
    override fun compareTo(other: PriceSimpleUI): Int {
        return tool.baseAsset.nameShort.compareTo(other.tool.baseAsset.nameShort)
    }

    fun pair() = "${tool.baseAsset.nameShort}/${tool.quoteAsset.nameShort}"

    fun toPriceSimple() = PriceSimple(
        tool = this.tool.copy(),
        price = this.price,
        pricePrev = this.pricePrev
    )

}