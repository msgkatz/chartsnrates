package com.msgkatz.ratesapp.feature.rootkmp.main

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

fun PriceSimpleUI.priceFormatted(): String = formatPrice(this.price)

fun formatPrice(price: Double, fraclen: Int = -1): String {
    // 1. Handle non-finite numbers
    if (price.isNaN()) {
        return "NaN"
    }
    if (price.isInfinite()) {
        // Double.toString() correctly produces "Infinity" or "-Infinity"
        return price.toString()
    }

    // 2. Convert the double to its initial string representation
    val valueString = price.toString()

    // 3. Basic check for scientific notation (E/e) - Limitation
    // This logic doesn't parse E-notation, so we return the original string if detected.
    if (valueString.contains('E', ignoreCase = true)) {
        // Consider logging a warning here in a real application
        // println("Warning: Input $price resulted in scientific notation '$valueString', returning unformatted.")
        return valueString // Cannot guarantee correct formatting for E-notation
    }

    // 4. Find the decimal point
    val decimalSeparatorIndex = valueString.indexOf('.')

    val integerPart: String
    val fractionalPartRaw: String

    if (decimalSeparatorIndex == -1) {
        // No decimal point found (e.g., "10")
        integerPart = valueString
        fractionalPartRaw = ""
    } else {
        // Decimal point found (e.g., "10.5", "-0.0")
        integerPart = valueString.substring(0, decimalSeparatorIndex)
        fractionalPartRaw = valueString.substring(decimalSeparatorIndex + 1)
    }

    // 5. Ensure minimum 2 fractional digits by padding with zeros if necessary
    val paddingNeeded = 2 - fractionalPartRaw.length
    val fractionalPartFormatted = if (paddingNeeded > 0) {
        fractionalPartRaw + "0".repeat(paddingNeeded)
    } else {
        if (fraclen == -1)
            fractionalPartRaw // Already has 2 or more digits
        else
            fractionalPartRaw.substring(0, fraclen)
    }

    // 6. Reconstruct the final string
    // Ensure the integer part isn't empty (e.g., for numbers like 0.5 -> "0.5")
    // Handle potential "-0" integer part from "-0.0".toString() correctly.
    val finalIntegerPart = if (integerPart.isEmpty() || integerPart == "-") {
        integerPart + "0" // Handles ".5" -> "0.5" or "-.5" -> "-0.5" (though toString usually doesn't produce this)
        // More accurately handles potential results like "0." or "-0." if they occurred
    } else {
        integerPart
    }


    // Always include the decimal point and the formatted fractional part
    return "$finalIntegerPart.$fractionalPartFormatted"
}