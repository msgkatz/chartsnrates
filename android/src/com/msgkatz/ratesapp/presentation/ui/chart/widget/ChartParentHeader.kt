package com.msgkatz.ratesapp.presentation.ui.chart.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.domain.entities.Tool
import com.msgkatz.ratesapp.presentation.theme.CnrThemeAlter
import com.msgkatz.ratesapp.presentation.theme.Green80
import com.msgkatz.ratesapp.presentation.theme.Red40
import com.msgkatz.ratesapp.presentation.ui.main.widget.GradientBackground
import com.msgkatz.ratesapp.utils.NumFormatUtil
import java.util.Locale

@Composable
fun ChartParentHeader(
    modifier: Modifier = Modifier,
    chartParentToolUIState: ChartParentToolUIState,
    chartParentPriceUIState: ChartParentPriceUIState,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        ChartAppBar(
            chartParentToolUIState = chartParentToolUIState,
            onBackClick = onBackClick,
        )

        Spacer(
            Modifier
                .height(10.dp)
                .windowInsetsTopHeight(WindowInsets.safeDrawing)
        )

        when (chartParentPriceUIState) {
            is ChartParentPriceUIState.Data -> {
                CurrentRateComposable(
                    modifier = Modifier.padding(start = 60.dp),
                    prevPrice = chartParentPriceUIState.prevPrice,
                    curPrice = chartParentPriceUIState.newPrice
                )
            }
            else -> {
                CurrentRateComposable(
                    modifier = Modifier.padding(start = 60.dp),
                    prevPrice = null,
                    //curPrice = chartParentPriceUIState.newPrice
                )
            }
        }


        Spacer(
            Modifier
                .height(18.dp)
                .windowInsetsTopHeight(WindowInsets.safeDrawing)
        )
        GradientBackground(modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(
            Modifier
                .height(18.dp)
                .windowInsetsTopHeight(WindowInsets.safeDrawing)
        )

    }

}

@Composable
fun CurrentRateComposable(
    modifier: Modifier = Modifier,
    prevPrice: Double?,
    curPrice: Double = 0.0,

) {
    val isLocalInspection = LocalInspectionMode.current
    val toUp = if (prevPrice == null || prevPrice <= curPrice) true else false
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        if (prevPrice == null || isLocalInspection) {
            Text(
                text = getPriceFormatted(curPrice),
                style = MaterialTheme.typography.headlineMedium,
            )
        } else {
            Text(
                text = getPriceFormatted(curPrice),
                style = MaterialTheme.typography.headlineMedium,
                color = if (toUp) { Green80 } else  { Red40 }
            )
        }
        if (prevPrice != null || isLocalInspection) {
            Row(
                modifier = Modifier.padding(top = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        //.background(MaterialTheme.colorScheme.surface)
                        .rotate(if (toUp) 270f else 90f)
                        .padding(horizontal = 3.dp)
                        .size(8.dp),

                    painter = painterResource(id = R.drawable.ic_triangle_right),
                    contentDescription = null,
                    tint = if (toUp) {
                        Green80
                    } else {
                        Red40
                    }
                    //tint = if (toUp) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error

                )

                Text(
                    text = getPriceDelta(prevPrice, curPrice),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (toUp) {
                        Green80
                    } else {
                        Red40
                    }
                    //color = if (toUp) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error
                )
            }
        }
    }


}

@Composable
private fun getPriceDelta(prevPrice: Double?, curPrice: Double) : String {
    val isLocalInspection = LocalInspectionMode.current
    if (isLocalInspection) return "0.20%"
    if (prevPrice == null) return ""
    val delta = 100 * Math.abs(curPrice - prevPrice) / prevPrice
    return String.format(Locale.getDefault(), "%.2f%%", delta)
}

@Composable
private fun getPriceFormatted(curPrice: Double): String {
    return NumFormatUtil.getFormattedPrice(curPrice)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartAppBar(
    modifier: Modifier = Modifier,
    chartParentToolUIState: ChartParentToolUIState,
    onBackClick: () -> Unit = {}
) {
    CnrAppBar(
        title = {
            when (chartParentToolUIState) {
                is ChartParentToolUIState.Loading ,
                is ChartParentToolUIState.Empty,
                -> {
                    Column() {
                        Row() {
                            Text("")
                            Text("")
                        }
                        Text("")
                    }
                }
                is ChartParentToolUIState.Data -> {
                    if (chartParentToolUIState.tool == null) {
                        Column() {
                            Row() {
                                Text("")
                                Text("")
                            }
                            Text("")
                        }
                    } else {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = chartParentToolUIState.tool.baseAsset.nameShort,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    modifier = Modifier.padding(bottom = 2.dp),
                                    text = "/${chartParentToolUIState.tool.quoteAsset.nameShort}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                text = chartParentToolUIState.tool.baseAsset.nameLong,
                                //style = MaterialTheme.typography.bodySmall
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = null,
                )
            }
        },
        //colors = TopAppBarColors(containerColor = Color.Transparent,),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            .copy(
                containerColor = Color.Transparent
            ),
        modifier = modifier,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CnrAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Cnr Top App Bar")
@Composable
private fun ChartAppBarPreview() {
    CnrAppBar(
        title = {
            Column() {
                Text(text = "Title")
                Text(text = "Title 2nd")
            }
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(4.dp),
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
            )
        },
        actions = {}
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview("ChartParentHeader")
@Composable
private fun ChartParentHeaderPreview(
    @PreviewParameter(ChartParentToolPreviewParameterProvider::class)
    toolList: List<Tool>,
) {
    CnrThemeAlter(
        darkTheme = true,
        androidTheme = false, //shouldUseAndroidTheme(uiState),
        disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
    ) {
        Surface {
            ChartParentHeader(
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
                onBackClick = {}
            )
        }
    }
}