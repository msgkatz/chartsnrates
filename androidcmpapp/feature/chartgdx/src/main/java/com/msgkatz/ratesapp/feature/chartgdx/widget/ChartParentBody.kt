package com.msgkatz.ratesapp.feature.chartgdx.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.defaultMinSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.core.uikit.theme.CnrThemeAlter
import com.msgkatz.ratesapp.old.data.entities.rest.AssetDT
import com.msgkatz.ratesapp.old.domain.entities.IntervalJava
import com.msgkatz.ratesapp.old.domain.entities.ToolJava
import com.msgkatz.ratesapp.old.utils.Parameters


@Composable
fun ColumnScope.ChartParentBody(
    modifier: Modifier = Modifier,
    chartParentToolUIState: ChartParentToolUIState,
    onIntervalClick: (IntervalJava) -> Unit
) {
    val isLocalInspection = LocalInspectionMode.current

    if (isLocalInspection) {
        Box(modifier = modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(text = "ChartGdxScreen")
        }
    } else {
        ChartGdxScreen(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth()

        )
    }

    Box(
        modifier = Modifier
            .height(100.dp)
            .align(Alignment.End)
    ) {
        when (chartParentToolUIState) {
            is ChartParentToolUIState.Loading, is ChartParentToolUIState.Empty -> {}
            is ChartParentToolUIState.Data -> {
                IntervalListComposable(
                    intervals = chartParentToolUIState.intervals,
                    onIntervalClick = onIntervalClick
                )
            }
        }
    }

}

@Composable
fun IntervalListComposable(
    modifier: Modifier = Modifier,
    intervals: List<IntervalJava>?,
    onIntervalClick: (IntervalJava) -> Unit
) {
    val selectedItem : MutableState<String> = rememberSaveable { mutableStateOf(Parameters.defaulScaletList[2].symbol) }
    LazyRow(
        modifier = modifier.fillMaxWidth().height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        intervals?.forEach {
            val id = it.symbol
            item(key = id) {
                IntervalListItem(
                    interval = it,
                    selectedItem = selectedItem,
                    onIntervalItemClick = { selectedItem.value = it.symbol; onIntervalClick(it) }
                )
            }

        }

    }
}

@Composable
fun IntervalListItem(
    modifier: Modifier = Modifier,
    interval: IntervalJava,
    selectedItem : MutableState<String>,
    onIntervalItemClick: () -> Unit
) {
    var isSelected = selectedItem.value.equals(interval.symbol)
    Box(
        modifier = modifier
            //.width(40.dp)
            .background(if (isSelected) MaterialTheme.colorScheme.onSecondary.copy(alpha = .5f) else Color.Transparent)
            .clickable(enabled = true, onClick = onIntervalItemClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            text = interval.symbol,
            style = if (isSelected) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
            //color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

}

@Preview
@Composable
fun IntervalListItemPreview() {
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {
        val interval = IntervalJava.fromString(Parameters.defaulScaletList[2].symbol)
        val selectedInterval = remember { mutableStateOf(Parameters.defaulScaletList[3].symbol) }
        Surface {
            IntervalListItem(
                interval = interval,
                selectedItem = selectedInterval,
                onIntervalItemClick = {}
            )
        }
    }
}

@Preview
@Composable
fun IntervalListPreview(
    @PreviewParameter(IntervalListPreviewParameterProvider::class)
    intervals: List<IntervalJava>
) {
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {
        Surface {
            IntervalListComposable(
                intervals = intervals,
                onIntervalClick = {}
            )
        }
    }
}

@Preview
@Composable
fun ChartParentBodyPreview(
    @PreviewParameter(IntervalListPreviewParameterProvider::class)
    intervals: List<IntervalJava>
) {
    val tool = ToolJava(
        "BTCBNB",
        AssetDT(0, "BTC", "Bitcoin"),
        AssetDT(0, "BNB", "Binance Coin")
    )
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {
        Surface {
            Column {
                ChartParentBody(
                    chartParentToolUIState = ChartParentToolUIState.Data(
                        tool,
                        intervals = intervals
                    ),
                    onIntervalClick = {}
                )
            }
        }
    }

}