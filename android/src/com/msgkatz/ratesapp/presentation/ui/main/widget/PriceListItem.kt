package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.data.entities.rest.Asset
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.domain.entities.Tool
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter

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
            Text(text = priceSimple.getBaseAssetNameLong())
        },
        trailingContent = {
            Column(modifier = Modifier
                //.fillMaxSize()
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
            ) {
                Text(text = priceSimple.priceFormatted)
                Row {
                    Icon(
                        modifier = modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .size(5.dp),
                        painter = painterResource(id = R.drawable.ic_triangle_right),
                        contentDescription = null,
                    )
                    Text(text = priceSimple.priceFormatted)
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

