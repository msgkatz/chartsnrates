package com.msgkatz.ratesapp.presentation.ui.app

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.core.uikit.theme.CnrThemeAlter


/**
 * Charts-n-Rates navigation bar item with icon and label content slots. Wraps Material 3
 * [NavigationBarItem].
 *
 * @param selected Whether this item is selected.
 * @param onClick The callback to be invoked when this item is selected.
 * @param icon The item icon content.
 * @param modifier Modifier to be applied to this item.
 * @param selectedIcon The item icon content when selected.
 * @param enabled controls the enabled state of this item. When `false`, this item will not be
 * clickable and will appear disabled to accessibility services.
 * @param label The item text label content.
 * @param alwaysShowLabel Whether to always show the label for this item. If false, the label will
 * only be shown when this item is selected.
 */
@Composable
fun RowScope.CnrNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CnrNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = CnrNavigationDefaults.navigationContentColor(),
            selectedTextColor = CnrNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = CnrNavigationDefaults.navigationContentColor(),
            indicatorColor = Color.Transparent, //CnrNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}


/**
 * Charts-n-Rates navigation bar with content slot. Wraps Material 3 [NavigationBar].
 *
 * @param modifier Modifier to be applied to the navigation bar.
 * @param content Destinations inside the navigation bar. This should contain multiple
 * [NavigationBarItem]s.
 */
@Composable
fun CnrNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {

        NavigationBar(
            modifier = modifier,
            containerColor = Color.Transparent,
            contentColor = CnrNavigationDefaults.navigationSelectedItemColor(),
            tonalElevation = 4.dp,
            content = content,
        )
}

@Preview
@Composable
fun CnrNavigationPreview() {
    val items = listOf("Usdt", "bnb", "btc", "eth")
    val icons = listOf(
        painterResource(id = R.drawable.cur_usdt),
        painterResource(id = R.drawable.cur_bnb),
        painterResource(id = R.drawable.cur_btc),
        painterResource(id = R.drawable.cur_eth),
    )

    CnrThemeAlter {
        CnrNavigationBar {
            items.forEachIndexed { index, item ->
                CnrNavigationBarItem(
                    icon = {
                        Icon(
                            painter = icons[index],
                            contentDescription = item,
                        )
                    },
                    selected = index == 0,
                    onClick = { },
                )
            }
        }
    }
}


/**
 * Charts-n-Rates navigation default values.
 */
object CnrNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}