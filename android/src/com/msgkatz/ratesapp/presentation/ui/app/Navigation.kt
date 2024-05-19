package com.msgkatz.ratesapp.presentation.ui.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.fastFirstOrNull
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import kotlin.math.roundToInt

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
            indicatorColor = CnrNavigationDefaults.navigationIndicatorColor(),
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
        contentColor = CnrNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content,
    )
}


///**
// * Base layout for a [NavigationBarItem].
// *
// * @param indicatorRipple indicator ripple for this item when it is selected
// * @param indicator indicator for this item when it is selected
// * @param icon icon for this item
// * @param label text label for this item
// * @param alwaysShowLabel whether to always show the label for this item. If false, the label will
// * only be shown when this item is selected.
// * @param animationProgress progress of the animation, where 0 represents the unselected state of
// * this item and 1 represents the selected state. This value controls other values such as indicator
// * size, icon and label positions, etc.
// */
//@Composable
//private fun NavigationBarItemLayout(
//    indicatorRipple: @Composable () -> Unit,
//    indicator: @Composable () -> Unit,
//    icon: @Composable () -> Unit,
//    label: @Composable (() -> Unit)?,
//    alwaysShowLabel: Boolean,
//    animationProgress: () -> Float,
//) {
//    Layout({
//        indicatorRipple()
//        indicator()
//
//        Box(Modifier.layoutId(IconLayoutIdTag)) { icon() }
//
//        if (label != null) {
//            Box(
//                Modifier
//                    .layoutId(LabelLayoutIdTag)
//                    .graphicsLayer { alpha = if (alwaysShowLabel) 1f else animationProgress() }
//                    .padding(horizontal = NavigationBarItemHorizontalPadding / 2)
//            ) { label() }
//        }
//    }) { measurables, constraints ->
//        @Suppress("NAME_SHADOWING")
//        val animationProgress = animationProgress()
//        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
//        val iconPlaceable =
//            measurables.fastFirst { it.layoutId == IconLayoutIdTag }.measure(looseConstraints)
//
//        val totalIndicatorWidth = iconPlaceable.width + (IndicatorHorizontalPadding * 2).roundToPx()
//        val animatedIndicatorWidth = (totalIndicatorWidth * animationProgress).roundToInt()
//        val indicatorHeight = iconPlaceable.height + (IndicatorVerticalPadding * 2).roundToPx()
//        val indicatorRipplePlaceable =
//            measurables
//                .fastFirst { it.layoutId == IndicatorRippleLayoutIdTag }
//                .measure(
//                    Constraints.fixed(
//                        width = totalIndicatorWidth,
//                        height = indicatorHeight
//                    )
//                )
//        val indicatorPlaceable =
//            measurables
//                .fastFirstOrNull { it.layoutId == IndicatorLayoutIdTag }
//                ?.measure(
//                    Constraints.fixed(
//                        width = animatedIndicatorWidth,
//                        height = indicatorHeight
//                    )
//                )
//
//        val labelPlaceable =
//            label?.let {
//                measurables
//                    .fastFirst { it.layoutId == LabelLayoutIdTag }
//                    .measure(looseConstraints)
//            }
//
//        if (label == null) {
//            placeIcon(iconPlaceable, indicatorRipplePlaceable, indicatorPlaceable, constraints)
//        } else {
//            placeLabelAndIcon(
//                labelPlaceable!!,
//                iconPlaceable,
//                indicatorRipplePlaceable,
//                indicatorPlaceable,
//                constraints,
//                alwaysShowLabel,
//                animationProgress
//            )
//        }
//    }
//}

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
//    val selectedIcons = listOf(
//        NiaIcons.Upcoming,
//        NiaIcons.Bookmarks,
//        NiaIcons.Grid3x3,
//    )

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
//                    selectedIcon = {
//                        Icon(
//                            imageVector = selectedIcons[index],
//                            contentDescription = item,
//                        )
//                    },
                    //label = { Text(item) },
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