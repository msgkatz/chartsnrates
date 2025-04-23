package com.msgkatz.ratesapp.feature.rootkmp.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.feature.rootkmp.main.CnrQuoteAssetDefaults.priceListItemColorGreen
import com.msgkatz.ratesapp.feature.rootkmp.main.CnrQuoteAssetDefaults.priceListItemColorRed
import com.msgkatz.ratesapp.feature.rootkmp.main.icons.Triangle
import com.msgkatz.ratesapp.feature.rootkmp.rootkmp.generated.resources.Res
import com.msgkatz.ratesapp.feature.rootkmp.rootkmp.generated.resources.cur_bnb
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs

//@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PriceListItemFlowed(
    priceSimple: PriceSimpleUI,
    imageUrl: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    priceListUIState: PriceListUIState,
    //iconPlaceholder: Placeholder,
    description: String = "",
) {
    val isLocalInspection = LocalInspectionMode.current
    ListItem(
        leadingContent = {
            PriceListItemIcon(
                imageUrl = imageUrl,
                priceListUIState = priceListUIState,
                //iconPlaceholder = iconPlaceholder,
                modifier = iconModifier.size(42.dp)
            )
        },
        headlineContent = {
            Text(text = priceSimple.pair())
        },
        supportingContent = {
            Text(
                text = priceSimple.tool.baseAsset.nameLong ?: "",
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
                val finalToUp = if (priceSimple.pricePrev == 0.0 || priceSimple.pricePrev < priceSimple.price) true
                else if (priceSimple.pricePrev > priceSimple.price) false
                else true

                if (priceSimple.pricePrev == 0.0 || isLocalInspection) {
                    Text(
                        text = priceSimple.priceFormatted(),
                        style = MaterialTheme.typography.labelLarge,
                    )
                } else {
                    Text(
                        text = priceSimple.priceFormatted(),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (finalToUp) { priceListItemColorGreen() } else { priceListItemColorRed() }
                    )
                }

                if (priceSimple.pricePrev != 0.0 || isLocalInspection) {
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
                            imageVector = Triangle,
                            //painter = painterResource(resource = Res.drawable.ic_triangle_right),
                            contentDescription = null,
                            tint = if (finalToUp) { priceListItemColorGreen() } else  { priceListItemColorRed() }
                            //tint = if (toUp) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error

                        )

                        Text(
                            text = getPriceDelta(priceSimple.pricePrev, priceSimple.price),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (finalToUp) { priceListItemColorGreen() } else  { priceListItemColorRed() }
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
    val delta = 100 * abs(curPrice - prevPrice) / prevPrice
    return formatPrice(delta)
}

//@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PriceListItemIcon(
    imageUrl: String,
    priceListUIState: PriceListUIState,
    //iconPlaceholder: Placeholder,
    modifier: Modifier = Modifier
) {
    if (imageUrl.isEmpty()
    || 1==1
    ) {
        Icon(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp),
            //imageVector =  NiaIcons.Person,
            painter = painterResource(resource = Res.drawable.cur_bnb),
            contentDescription = null, // decorative image
        )
    } else {
//        DynamicAsyncImage(
//            imageUrl = imageUrl,
//            contentDescription = null,
//            modifier = modifier,
//            priceListUIState = priceListUIState,
//            //placeholder = iconPlaceholder,
//        )
    }
}

object CnrQuoteAssetDefaults {
    @Composable
    fun priceListItemColorRed() = Color(0xFFBA1A1A)

    @Composable
    fun priceListItemColorGreen() = Color(0xFF0EE37C)
}

