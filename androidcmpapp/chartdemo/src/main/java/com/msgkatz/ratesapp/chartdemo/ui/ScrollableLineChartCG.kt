package com.msgkatz.ratesapp.chartdemo.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

//import kotlinx.coroutines.flow.snapshotFlow

@Composable
fun ScrollableLineChart(
    dataPoints: List<Float>,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Blue,
    lineWidth: Float = 4f,
    pointSpacing: Float = 50f, // space between each data point
    chartHeight: Float = 200f
) {
    // Create a horizontal scroll state
    val scrollState = rememberScrollState()

    // Trigger loading more data when scrolling near the end
    LaunchedEffect(scrollState.value, scrollState.maxValue) {
        snapshotFlow { scrollState.value }
            .collect { currentScroll ->
                if (currentScroll >= scrollState.maxValue - 100) {
                    onLoadMore()
                }
            }
    }

//    val offset = remember { Animatable(0f) }
//    LaunchedEffect(offset) {
//        offset.animateTo(targetValue = horizontalOffset)
//    }

    // Calculate the total width based on the number of data points
    val canvasWidth = (dataPoints.size - 1) * pointSpacing

    Box(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(
                state = scrollState,
//                flingBehavior = object : FlingBehavior {
//                    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
//                        offset.animateDecay(initialVelocity, animationSpec = exponentialDecay())
//                    }
//                }
            )
            .background(Color.LightGray) // background for clarity
    ) {
        Canvas(
            modifier = Modifier
                .height(chartHeight.dp)
                // Set the canvas width so that it reflects your entire data set.
                .then(Modifier.width(canvasWidth.dp))
        ) {
            if (dataPoints.isEmpty()) return@Canvas

            // Compute min and max for scaling the y-axis
            val maxData = dataPoints.maxOrNull() ?: 0f
            val minData = dataPoints.minOrNull() ?: 0f
            val dataRange = maxData - minData

            // Map data to canvas points
            val points = dataPoints.mapIndexed { index, data ->
                val x = index * pointSpacing
                // Invert y: higher values appear toward the top of the canvas.
                val y = if (dataRange != 0f) {
                    size.height - ((data - minData) / dataRange * size.height)
                } else {
                    size.height / 2
                }
                Offset(x, y)
            }

            // Draw lines connecting each point
            points.zipWithNext { start, end ->
                drawLine(
                    color = lineColor,
                    start = start,
                    end = end,
                    strokeWidth = lineWidth,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}