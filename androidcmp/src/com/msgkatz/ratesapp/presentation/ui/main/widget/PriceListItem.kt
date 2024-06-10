package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.data.entities.rest.Asset
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.domain.entities.Tool
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.theme.Green30
import com.msgkatz.ratesapp.presentation.theme.Green80
import com.msgkatz.ratesapp.presentation.theme.Red40
import com.msgkatz.ratesapp.utils.NumFormatUtil
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PriceListItem(
    priceSimple: PriceSimple,
    imageUrl: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    iconPlaceholder: Placeholder,
    description: String = "",
) {
    val isLocalInspection = LocalInspectionMode.current
    ListItem(
        leadingContent = {
            PriceListItemIcon(
                imageUrl = imageUrl,
                iconPlaceholder = iconPlaceholder,
                modifier = iconModifier.size(42.dp)
            )
        },
        headlineContent = {
            Text(text = priceSimple.getPair())
        },
        supportingContent = {
            Text(
                text = priceSimple.getBaseAssetNameLong(),
                color = MaterialTheme.colorScheme.outline //surfaceVariant
            )
        },
        trailingContent = {
            Column(
                modifier = Modifier,
                //.fillMaxSize()
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
            ) {
                val prevPrice = rememberPrevious(priceSimple.price)
                val toUp = if (prevPrice == null || prevPrice < priceSimple.price) PriceDiff.Up
                            else if (prevPrice > priceSimple.price) PriceDiff.Down
                            else PriceDiff.Same
                val prevToUp: PriceDiff? = rememberPrevious(toUp) { prev, cur ->
                    cur != PriceDiff.Same && prev != cur
                }

                val finalToUp = if (toUp == PriceDiff.Up || (toUp == PriceDiff.Same && prevToUp == PriceDiff.Up)) true
                                else if (toUp == PriceDiff.Down || (toUp == PriceDiff.Same && prevToUp == PriceDiff.Down)) false
                                else false

                if (prevPrice == null || isLocalInspection) {
                    Text(
                        text = priceSimple.priceFormatted,
                        style = MaterialTheme.typography.labelLarge,
                    )
                } else {
                    Text(
                        text = priceSimple.priceFormatted,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (finalToUp) { Green80 } else { Red40 }
                    )
                }

                if (prevPrice != null || isLocalInspection) {
                    Row(
                        modifier = Modifier.padding(top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = modifier
                                //.background(MaterialTheme.colorScheme.surface)
                                .rotate(if (finalToUp) 270f else 90f)
                                .padding(horizontal = 3.dp)
                                .size(6.dp),

                            painter = painterResource(id = R.drawable.ic_triangle_right),
                            contentDescription = null,
                            tint = if (finalToUp) { Green80 } else  { Red40 }
                            //tint = if (toUp) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error

                        )

                        Text(
                            text = getPriceDelta(prevPrice, priceSimple.price),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (finalToUp) { Green80 } else  { Red40 }
                            //color = if (toUp) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error
                        )
                    }
                }

            }
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        modifier = modifier
            .semantics(mergeDescendants = true) { /* no-op */ }
            .clickable(enabled = true, onClick = onItemClick),

    )
}

@Composable
private fun getPriceDelta(prevPrice: Double?, curPrice: Double) : String {
    val isLocalInspection = LocalInspectionMode.current
    if (isLocalInspection) return "0.20%"
    if (prevPrice == null) return ""
    val delta = 100 * Math.abs(curPrice - prevPrice) / prevPrice
    return String.format(Locale.getDefault(), "%.2f%%", delta)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PriceListItemIcon(imageUrl: String, iconPlaceholder: Placeholder, modifier: Modifier = Modifier) {
    if (imageUrl.isEmpty()) {
        Icon(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp),
            //imageVector =  NiaIcons.Person,
            painter = painterResource(id = R.drawable.cur_bnb),
            contentDescription = null, // decorative image
        )
    } else {
        DynamicAsyncImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = modifier,
            placeholder = iconPlaceholder
        )
    }
}


/**
 * Returns a dummy MutableState that does not cause render when setting it
 */
@Composable
fun <T> rememberRef(): MutableState<T?> {
    // for some reason it always recreated the value with vararg keys,
    // leaving out the keys as a parameter for remember for now
    return remember() {
        object: MutableState<T?> {
            override var value: T? = null

            override fun component1(): T? = value

            override fun component2(): (T?) -> Unit = { value = it }
        }
    }
}

/**
 * <p> Version of partial update via remembering of prev value for comparing with new one before recomposition.
 * See <a href="https://stackoverflow.com/questions/68046535/lazycolumn-and-mutable-list-how-to-update">this</a> for info.
 * <p/>
 */
@Composable
fun <T> rememberPrevious(
    current: T,
    shouldUpdate: (prev: T?, curr: T) -> Boolean = { a: T?, b: T -> a != b },
): T? {
    val ref = rememberRef<T>()

    // launched after render, so the current render will have the old value anyway
    SideEffect {
        if (shouldUpdate(ref.value, current)) {
            ref.value = current
        }
    }

    return ref.value
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
private fun PriceListItemPreview() {

    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {

        Surface {
            PriceListItem(
                priceSimple = PriceSimple(Tool("BTCBNB", Asset(0,"BTC", "Bitcoin"), Asset(0,"BNB", "Binance Coin")), 23.105),
                imageUrl = "",
                onItemClick = { },
                iconPlaceholder = placeholder(R.drawable.cur_bnb),

            )
        }
    }
}

sealed class PriceDiff {
    data object Up: PriceDiff()
    data object Same: PriceDiff()
    data object Down: PriceDiff()
}

