package com.msgkatz.ratesapp.chartdemo.ui.chart2

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun DynamicCryptoChart(
    modifier: Modifier = Modifier,
    viewModel: CryptoChartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dataPoints = uiState.dataPoints

    var viewPortSize by remember { mutableStateOf(IntSize.Zero) }
    var totalChartWidthPx by remember { mutableStateOf(0f) }
    val scrollOffset = remember { mutableStateOf(0f) }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // Pixels per hour - determines zoom and total width
    val pixelsPerHour = with(density) { 4.dp.toPx() } // Increased zoom slightly

    // Update total width whenever data changes
    LaunchedEffect(dataPoints) {
        if (dataPoints.isNotEmpty()) {
            val firstTs = dataPoints.first().timestampMillis
            val lastTs = dataPoints.last().timestampMillis
            val durationMillis = (lastTs - firstTs).coerceAtLeast(0)
            val durationHours = durationMillis / (1000f * 60 * 60)
            // Ensure minimum width even for small duration to make scrolling work
            totalChartWidthPx = (durationHours * pixelsPerHour).coerceAtLeast(viewPortSize.width.toFloat())

            // Adjust scroll offset if it becomes invalid after data load
            val maxOffset = (totalChartWidthPx - viewPortSize.width).coerceAtLeast(0f)

            // --- Behavior after load: ---
            // Option A: Jump to show the newly loaded data (if loading past, scroll to 0)
            if (uiState.loadingDirection == LoadingDirection.PAST) {
                // Simple approach: If past data was loaded, keep view relative to the *start*
                // This might feel like a jump if a lot was loaded. Needs refinement for smooth transition.
                // scrollOffset.value = 0f // Reset scroll to see the oldest data immediately
            }
            // Option B: Try to keep the current view stable (adjust offset based on added width)
            // Needs more complex logic based on where data was added.

            // Option C: Ensure offset is just clamped (simplest)
            scrollOffset.value = scrollOffset.value.coerceIn(0f, maxOffset)


        } else {
            totalChartWidthPx = 0f
            scrollOffset.value = 0f
        }
        println("CHART_COMP: Data updated. Total Width: $totalChartWidthPx px")
    }

    // Recalculate max offset whenever total width or viewport changes
    val maxScrollOffset by remember(totalChartWidthPx, viewPortSize) {
        derivedStateOf { (totalChartWidthPx - viewPortSize.width).coerceAtLeast(0f) }
    }

    val scrollableState = rememberScrollableState { delta ->
        val newOffset = (scrollOffset.value - delta).coerceIn(0f, maxScrollOffset)
        val consumed = scrollOffset.value - newOffset // How much offset actually changed
        scrollOffset.value = newOffset

        // --- Trigger preloading (only if not already loading) ---
        if (!uiState.isLoading) {
            val loadThresholdPx = viewPortSize.width * 0.3f // 30% threshold
            if (uiState.canLoadPast && scrollOffset.value < loadThresholdPx) {
                println("CHART_COMP: Near left edge, requesting past data...")
                viewModel.requestLoadMorePastData()
            } else if (uiState.canLoadFuture && scrollOffset.value > maxScrollOffset - loadThresholdPx) {
                println("CHART_COMP: Near right edge, requesting future data...")
                viewModel.requestLoadMoreFutureData()
            }
        }

        -consumed // Return amount consumed (negative of offset change)
    }


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.DarkGray.copy(alpha = 0.1f))
            .onSizeChanged { viewPortSize = it }
            .scrollable(
                orientation = Orientation.Horizontal,
                state = scrollableState,
            )
    ) {
        if (dataPoints.isEmpty() && uiState.isLoading) {
            // Initial loading state
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (dataPoints.isEmpty()) {
            Text(
                "No data available",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            // --- Chart Canvas (passing necessary state) ---
            ChartCanvas(
                modifier = Modifier
                    .fillMaxHeight()
                    // Important: Use derived total width for canvas size
                    .width(with(density) { totalChartWidthPx.toDp() }),
                dataPoints = dataPoints,
                scrollOffsetPx = scrollOffset.value,
                viewPortWidthPx = viewPortSize.width,
                pixelsPerHour = pixelsPerHour
            )

            // --- Loading Indicator (Bound to Edge) ---
            if (uiState.isLoading) {
                // Position spinner at the edge corresponding to the loading direction
                val alignment = when (uiState.loadingDirection) {
                    LoadingDirection.PAST -> Alignment.CenterStart
                    LoadingDirection.FUTURE -> Alignment.CenterEnd
                    LoadingDirection.NONE -> Alignment.Center // Fallback, shouldn't happen while isLoading=true
                }
                // Add padding to bring it slightly inwards from the absolute edge
                val padding = Modifier.padding(horizontal = 16.dp)

                CircularProgressIndicator(
                    modifier = Modifier
                        .align(alignment)
                        .then(padding) // Apply padding
                        .size(30.dp),
                    strokeWidth = 3.dp
                )
            }

            // --- Optional: Debug Info ---
            // DebugText(scrollOffset.value, totalChartWidthPx, viewPortSize.width, maxScrollOffset, dataPoints.size)

        } // End of else (dataPoints not empty)
    } // End of Box
}

@Composable
fun ChartCanvas(
    modifier: Modifier = Modifier,
    dataPoints: List<CryptoDataPoint>,
    scrollOffsetPx: Float,
    viewPortWidthPx: Int,
    pixelsPerHour: Float
) {
    // Skip drawing if no data or viewport not measured yet
    if (dataPoints.isEmpty() || viewPortWidthPx <= 0 || pixelsPerHour <= 0f) return

    val density = LocalDensity.current
    val textPaint = remember { /* ... (unchanged) ... */
        android.graphics.Paint().apply {
            color = android.graphics.Color.LTGRAY
            textSize = with(density) { 10.sp.toPx() }
            textAlign = android.graphics.Paint.Align.CENTER
        }
    }
    val gridLinePaint = remember { /* ... (unchanged) ... */
        Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f)))
    }
    val lineThickness = with(density) { 1.5.dp.toPx()}
    val axisTextPadding = with(density) { 4.dp.toPx() }


    // --- Calculate Visible Time Range + Buffer ---
    val firstTimestamp = remember(dataPoints) { dataPoints.first().timestampMillis }
    // Calculate visible start/end time based on scroll offset
    val visibleStartTime = remember(firstTimestamp, scrollOffsetPx, pixelsPerHour) {
        firstTimestamp + (scrollOffsetPx / pixelsPerHour * 3600_000L).toLong()
    }
    val visibleDurationMillis = remember(viewPortWidthPx, pixelsPerHour) {
        (viewPortWidthPx / pixelsPerHour * 3600_000L).toLong()
    }
    val visibleEndTime = remember(visibleStartTime, visibleDurationMillis) {
        visibleStartTime + visibleDurationMillis
    }

    // Add buffer (e.g., 50% of visible duration on each side)
    val bufferMillis = remember(visibleDurationMillis) { (visibleDurationMillis * 0.5f).toLong() }
    val bufferedStartTime = remember(visibleStartTime, bufferMillis) { visibleStartTime - bufferMillis }
    val bufferedEndTime = remember(visibleEndTime, bufferMillis) { visibleEndTime + bufferMillis }

    // --- Calculate Min/Max Price based on buffered visible data ---
    val (minPrice, maxPrice) = remember(dataPoints, bufferedStartTime, bufferedEndTime) {
        val visibleData = dataPoints.filter {
            it.timestampMillis in bufferedStartTime..bufferedEndTime
        }
        val minP = visibleData.minOfOrNull { it.price }
        val maxP = visibleData.maxOfOrNull { it.price }

        // Handle cases: no visible data, or min/max are the same
        when {
            minP == null || maxP == null -> Pair(0f, 1f) // Default fallback
            minP == maxP -> Pair(minP * 0.95f, maxP * 1.05f) // Add small padding if flat
            else -> Pair(minP, maxP)
        }
    }
    val priceRange = remember(minPrice, maxPrice) {
        (maxPrice - minPrice).coerceAtLeast(0.01f) // Avoid division by zero, ensure min range
    }

    Canvas(modifier = modifier) {
        val canvasHeight = size.height
        val canvasWidth = size.width // This is the totalChartWidthPx

        // --- Transformation functions (Y depends on dynamic min/max) ---
        fun getX(timestampMillis: Long): Float {
            val hoursSinceStart = (timestampMillis - firstTimestamp) / (1000f * 60 * 60)
            // NO scroll offset subtraction here - draw at absolute position
            return hoursSinceStart * pixelsPerHour
        }

        fun getY(price: Float): Float {
            val priceRatio = ((price - minPrice) / priceRange).coerceIn(0f, 1f) // Clamp ratio
            val padding = canvasHeight * 0.1f
            return canvasHeight - (padding + priceRatio * (canvasHeight - 2 * padding))
        }

        // --- Drawing ---
        val chartPath = Path()

        // --- Optimization: Find first and last potentially visible index ---
        // Estimate indices roughly based on time; avoids iterating the whole list for path calculation
        // This requires data to be sorted by time
        val firstVisibleIdx = dataPoints.indexOfFirst { it.timestampMillis >= bufferedStartTime }
            .coerceAtLeast(0)
        val lastVisibleIdx = dataPoints.indexOfLast { it.timestampMillis <= bufferedEndTime }
            .coerceAtLeast(0).coerceAtMost(dataPoints.size - 1)

        // Iterate only over the potentially visible range + 1 point buffer for line continuity
        val startIdx = (firstVisibleIdx - 1).coerceAtLeast(0)
        val endIdx = (lastVisibleIdx + 1).coerceAtMost(dataPoints.size - 1)

        // --- Build Path for visible segment ---
        for (index in startIdx..endIdx) {
            val point = dataPoints[index]
            val x = getX(point.timestampMillis)
            val y = getY(point.price)

            // Use move To for the first point in our drawing segment OR if path was reset
            if (index == startIdx || chartPath.isEmpty) {
                chartPath.moveTo(x, y)
            } else {
                chartPath.lineTo(x, y)
            }
        }


        // Apply translation to the canvas draw scope to handle scrolling
        // This is simpler than adjusting every single draw call's coordinates
        translate(left = -scrollOffsetPx) {

            // Draw the price line Path
            drawPath(
                path = chartPath,
                color = Color(0xFF4CAF50),
                style = Stroke(width = lineThickness)
            )

            // --- Draw Axes & Grid (Now drawn at absolute X, scrolled via translate) ---

            // Draw Y-axis labels & Grid (Price)
            val yLabelCount = 5
            (0..yLabelCount).forEach { i ->
                val price = minPrice + (priceRange / yLabelCount) * i
                val y = getY(price)
                // Draw grid line across the entire chart width
                drawLine(
                    color = Color.Gray.copy(alpha = 0.5f),
                    start = Offset(0f, y), // Start at absolute 0
                    end = Offset(canvasWidth , y), // End at absolute total width
                    strokeWidth = gridLinePaint.width,// .strokeWidth,
                    pathEffect = gridLinePaint.pathEffect
                )
                // Draw label - position relative to the *scrolled* viewport edge
                drawContext.canvas.nativeCanvas.drawText(
                    "%.0f".format(price),
                    scrollOffsetPx + axisTextPadding, // Attach label to left edge of viewport
                    y + axisTextPadding, // Center vertically slightly
                    textPaint.apply { textAlign = android.graphics.Paint.Align.LEFT }
                )
            }

            // Draw X-axis labels & Grid (Time)
            //val timeLabelFormatter = remember { SimpleDateFormat("HH:mm dd/MM", Locale.getDefault()) }
            val timeLabelFormatter = SimpleDateFormat("HH:mm dd/MM", Locale.getDefault())
//            val timeStepMillis = remember(viewPortWidthPx, pixelsPerHour) {
//                estimateTimeStep(viewPortWidthPx, pixelsPerHour)
//            }
            val timeStepMillis = estimateTimeStep(viewPortWidthPx, pixelsPerHour)


            // Iterate through time steps covering the *entire* chart duration
            val chartEndTime = dataPoints.last().timestampMillis
            var currentTimeLabel = (firstTimestamp / timeStepMillis) * timeStepMillis
            while (currentTimeLabel <= chartEndTime + timeStepMillis) { // Iterate across the whole data range
                val x = getX(currentTimeLabel)

                // Draw grid line
                drawLine(
                    color = Color.Gray.copy(alpha = 0.5f),
                    start = Offset(x, 0f),
                    end = Offset(x, canvasHeight),
                    strokeWidth = gridLinePaint.width, // .strokeWidth,
                    pathEffect = gridLinePaint.pathEffect
                )

                // Draw label - Only draw if the label's X position is within the visible viewport
                if (x >= scrollOffsetPx - viewPortWidthPx*0.1f && x <= scrollOffsetPx + viewPortWidthPx*1.1f) { // Check if within visible area + buffer
                    drawContext.canvas.nativeCanvas.drawText(
                        timeLabelFormatter.format(Date(currentTimeLabel)),
                        x, // Position at the absolute X coordinate
                        canvasHeight - axisTextPadding, // Position near bottom
                        textPaint.apply { textAlign = android.graphics.Paint.Align.CENTER }
                    )
                }

                currentTimeLabel += timeStepMillis
                if (timeStepMillis == 0L) break
            }
        } // End of translate block
    }
}


// Helper function (Unchanged)
fun estimateTimeStep(viewPortWidthPx: Int, pixelsPerHour: Float): Long {
    // ... (implementation from previous example) ...
    if (pixelsPerHour <= 0f) return 24 * 3600 * 1000L // Avoid division by zero, fallback
    val visibleDurationHours = viewPortWidthPx / pixelsPerHour
    val visibleDurationMillis = visibleDurationHours * 3600_000L

    return when {
        visibleDurationMillis <= 1 * 3600_000L -> 5 * 60 * 1000L // 5 mins for very high zoom
        visibleDurationMillis <= 4 * 3600_000L -> 15 * 60 * 1000L // 15 mins
        visibleDurationMillis <= 12 * 3600_000L -> 1 * 3600_000L // 1 hour
        visibleDurationMillis <= 2 * 24 * 3600_000L -> 3 * 3600_000L // 3 hours
        visibleDurationMillis <= 7 * 24 * 3600_000L -> 12 * 3600_000L // 12 hours
        visibleDurationMillis <= 30 * 24 * 3600_000L -> 1 * 24 * 3600_000L // 1 day
        else -> 7 * 24 * 3600_000L // 1 week
    }
}


// Optional Debug Text (add maxScrollOffset)
@Composable
fun BoxScope.DebugText(offset: Float, totalWidth: Float, viewWidth: Int, maxOffset: Float, points: Int) {
    Text(
        text = "Offset: ${offset.roundToInt()}px / ${maxOffset.roundToInt()}px\nTotalW: ${totalWidth.roundToInt()}px | ViewW: $viewWidth px\nDataPoints: $points",
        color = Color.Black,
        fontSize = 10.sp,
        modifier = Modifier
            .align(Alignment.TopStart)
            .background(Color.Yellow.copy(alpha = 0.7f))
            .padding(2.dp)
    )
}