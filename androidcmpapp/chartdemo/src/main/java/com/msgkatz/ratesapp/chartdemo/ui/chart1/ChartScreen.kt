package com.msgkatz.ratesapp.chartdemo.ui.chart1

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChartScreen(
    modifier: Modifier = Modifier,
) {
    // This is your mutable state that holds the data points
    var chartData by remember { mutableStateOf(listOf(5f, 10f, 7f, 12f, 9f, 15f)) }

    // Callback to load more data. In a real app, this might be an API call or a database query.
    fun loadMoreData() {
        // Append extra data points. This is just a dummy implementation.
        chartData = chartData + listOf(
            (chartData.last() + (1..5).random()).toFloat(),
            (chartData.last() + (1..5).random()).toFloat(),
            (chartData.last() + (1..5).random()).toFloat()
        )
    }

    ScrollableLineChart(
        dataPoints = chartData,
        onLoadMore = { loadMoreData() },
        modifier = modifier,
        lineColor = Color.Green,
        lineWidth = 6f,
        pointSpacing = 50f,
        chartHeight = 200f
    )
}

@Composable
fun ChartScreen2(
    modifier: Modifier = Modifier,
    ) {
    ScrollableChartWithLazyRow(modifier = modifier)
}

@Composable
@Preview
fun ChartScreenPreview() {
    ChartScreen()
}

@Composable
@Preview
fun ChartScreen2Preview() {
    ChartScreen2()
}
