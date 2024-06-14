package com.msgkatz.ratesapp.presentation.ui.chart.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.ViewModelStoreOwner
import com.msgkatz.ratesapp.domain.entities.Interval
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.domain.entities.Tool
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.ui.app.InterimVMKeeper
import com.msgkatz.ratesapp.presentation.ui.main.widget.QuoteAssetScreen
import com.msgkatz.ratesapp.presentation.ui.main.widget.QuoteAssetViewModel

@Composable
fun ChartParentRoute(
    toolName: String,
    toolPrice: Double,
    modifier: Modifier = Modifier,
    interimVMKeeper : InterimVMKeeper,
    onBackClick: () -> Unit,
    onIntervalClick: (Interval) -> Unit,
    owner: ViewModelStoreOwner,

    ) {
    val viewModel: ChartParentViewModel = interimVMKeeper.makeChartParentViewModel(toolName, toolPrice, owner)
    val chartParentToolUiState by viewModel.chartParentToolUiState.collectAsState()
    val chartParentPriceUIState by viewModel.chartParentPriceUIState.collectAsState()

    ChartParentScreen(
        modifier = modifier,
        chartParentToolUIState = chartParentToolUiState,
        chartParentPriceUIState = chartParentPriceUIState,
        onBackClick = onBackClick,
        onIntervalClick = onIntervalClick,
    )

}

@Composable
fun ChartParentScreen(
    modifier: Modifier = Modifier,
    chartParentToolUIState: ChartParentToolUIState,
    chartParentPriceUIState: ChartParentPriceUIState,
    onBackClick: () -> Unit,
    onIntervalClick: (Interval) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        ChartParentHeader(
            chartParentToolUIState = chartParentToolUIState,
            chartParentPriceUIState = chartParentPriceUIState,
            onBackClick = onBackClick,
        )
        ChartParentBody(
            chartParentToolUIState = chartParentToolUIState,
            onIntervalClick = onIntervalClick,
        )
    }
}

@Preview("ChartParentScreen")
@Composable
private fun ChartParentScreenPreview(
    @PreviewParameter(ChartParentToolPreviewParameterProvider::class)
    toolList: List<Tool>,
) {
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {
        Surface {
            ChartParentScreen(
                chartParentToolUIState = ChartParentToolUIState
                    .Data(
                        tool = toolList[0],
                        intervals = null
                    ),
                chartParentPriceUIState = ChartParentPriceUIState
                    .Data(
                        prevPrice = 23.104,
                        newPrice = 23.200
                    ),
                onBackClick = {},
                onIntervalClick = {},
            )
        }
    }
}