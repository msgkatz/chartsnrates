package com.msgkatz.ratesapp.chartdemo.ui.chart2

// Composable Imports (ensure all necessary imports are present)
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
// ... other imports (Box, background, scrollable, layout, material3, runtime, ui, etc.)
import androidx.compose.material3.CircularProgressIndicator // Ensure this is imported
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds // Import clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
// ... other graphics imports
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun DynamicCryptoChart2(
    modifier: Modifier = Modifier,
    viewModel: CryptoChartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dataPoints = uiState.dataPoints

    var viewPortSize by remember { mutableStateOf(IntSize.Zero) }
    var totalChartWidthPx by remember { mutableStateOf(0f) }
    val scrollOffset = remember { mutableStateOf(0f) } // Represents the value subtracted from absolute X
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // Pixels per hour - determines zoom and total width. Ensure consistency with ChartCanvas.
    val pixelsPerHour = with(density) { 4.dp.toPx() }

    // --- State for Option B: Remember previous start timestamp ---
    var previousFirstTimestamp by remember { mutableStateOf<Long?>(null) }

    // Update total width AND adjust scroll offset when data changes
    LaunchedEffect(dataPoints, viewPortSize.width, pixelsPerHour) {
        // Get the timestamp of the first data point *in the current list*
        val currentFirstTimestamp = dataPoints.firstOrNull()?.timestampMillis

        if (dataPoints.isNotEmpty() && currentFirstTimestamp != null) {
            // --- Calculate new total width ---
            val lastTs = dataPoints.last().timestampMillis
            // Calculate duration based on the actual first and last points in the current list
            val durationMillis = (lastTs - currentFirstTimestamp).coerceAtLeast(0)
            val durationHours = durationMillis / (3600_000f) // 1000ms * 60s * 60m
            // Ensure minimum width, e.g., viewport width, to allow scrolling even with few points
            val newTotalWidthPx = (durationHours * pixelsPerHour).coerceAtLeast(viewPortSize.width.toFloat())

            var adjustmentOffsetPx = 0f // How much to add to the current scroll offset

            // --- Option B Logic: Check if past data was added ---
            // This check is crucial: Did the first timestamp *change* and become *earlier*?
            if (previousFirstTimestamp != null && currentFirstTimestamp < previousFirstTimestamp!!) {
                // Past data was loaded. Calculate the width added at the beginning.
                val addedDurationMillis = previousFirstTimestamp!! - currentFirstTimestamp
                val addedDurationHours = addedDurationMillis / (3600_000f)
                val addedWidthPx = (addedDurationHours * pixelsPerHour).coerceAtLeast(0f)

                // We need to increase the current scroll offset by the width that was added to the left
                // to keep the same data visually centered (or wherever it was).
                adjustmentOffsetPx = addedWidthPx
                println("CHART_COMP: Past data loaded. Adjusting scroll by +${addedWidthPx.roundToInt()} px")
            }

            // --- Update State ---
            totalChartWidthPx = newTotalWidthPx // Update the total width state

            // Calculate new max offset based on the *new* total width
            val maxOffset = (newTotalWidthPx - viewPortSize.width).coerceAtLeast(0f)

            // Apply adjustment to the current scroll offset, then clamp within new bounds
            // Add the calculated adjustment BEFORE clamping
            scrollOffset.value = (scrollOffset.value + adjustmentOffsetPx).coerceIn(0f, maxOffset)

            // --- Update previous timestamp for the *next* comparison ---
            // This MUST happen after the comparison and offset calculation for this run
            previousFirstTimestamp = currentFirstTimestamp

        } else { // No data points or currentFirstTimestamp is null
            totalChartWidthPx = 0f
            scrollOffset.value = 0f
            previousFirstTimestamp = null // Reset if data becomes empty
        }
        // println("CHART_COMP: Data updated. Total Width: $totalChartWidthPx px, Scroll: ${scrollOffset.value}") // Optional Debug
    }


    // Recalculate max offset whenever total width or viewport changes (for scrollableState)
    val maxScrollOffset by remember(totalChartWidthPx, viewPortSize) {
        derivedStateOf { (totalChartWidthPx - viewPortSize.width).coerceAtLeast(0f) }
    }

    val scrollableState = rememberScrollableState { delta ->
        // Clamp new offset immediately within the current max bounds
        val newOffset = (scrollOffset.value - delta).coerceIn(0f, maxScrollOffset)
        // Calculate how much the offset actually changed
        val consumed = scrollOffset.value - newOffset
        // Update the scroll offset state
        scrollOffset.value = newOffset

        // --- Trigger preloading (only if not already loading) ---
        if (!uiState.isLoading) {
            val loadThresholdPx = viewPortSize.width * 0.3f // 30% threshold
            // Check if scrolling near the left edge AND we can load more past data
            if (uiState.canLoadPast && scrollOffset.value < loadThresholdPx) {
                println("CHART_COMP: Near left edge, requesting past data...")
                viewModel.requestLoadMorePastData()
                // Check if scrolling near the right edge AND we can load more future data
            } else if (uiState.canLoadFuture && scrollOffset.value > maxScrollOffset - loadThresholdPx) {
                println("CHART_COMP: Near right edge, requesting future data...")
                viewModel.requestLoadMoreFutureData()
            }
        }
        // Return the amount of scroll delta that was consumed (negative of offset change)
        -consumed
    }


    Box( // Main container
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp) // Or desired height
            .background(Color.DarkGray.copy(alpha = 0.1f))
            .onSizeChanged { viewPortSize = it }
            .scrollable( // Apply scroll handling
                orientation = Orientation.Horizontal,
                state = scrollableState,
            )
            .clipToBounds() // Clip content (like canvas) to the Box bounds
    ) {
        // --- Conditional Content: Loading / No Data / Chart ---
        if (dataPoints.isEmpty() && uiState.isLoading) {
            // Initial loading state: Show centered spinner
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (dataPoints.isEmpty()) {
            // No data available state
            Text(
                "No data available",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            // --- Chart Canvas (use the stable version from before) ---
            ChartCanvas(
                modifier = Modifier
                    .fillMaxHeight()
                    // Set the total width based on calculated state
                    .width(with(density) { totalChartWidthPx.toDp() }),
                dataPoints = dataPoints,
                scrollOffsetPx = scrollOffset.value, // Pass current scroll offset
                viewPortWidthPx = viewPortSize.width,
                pixelsPerHour = pixelsPerHour
                // isLoading/loadingDirection not passed to this Canvas version
            )

            // --- Loading Indicator (Bound to Edge - Restored) ---
            // Show spinner if loading is in progress (even if data isn't empty yet)
            if (uiState.isLoading) {
                // Determine alignment based on the direction of the *current* load
                val alignment = when (uiState.loadingDirection) {
                    LoadingDirection.PAST -> Alignment.CenterStart
                    LoadingDirection.FUTURE -> Alignment.CenterEnd
                    LoadingDirection.NONE -> Alignment.Center // Fallback if direction is somehow NONE while loading
                }
                // Add some padding to bring the spinner slightly inwards
                val padding = Modifier.padding(horizontal = 16.dp)
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(alignment) // Align to the start or end of the Box
                        .then(padding) // Apply padding
                        .size(30.dp), // Set spinner size
                    strokeWidth = 3.dp // Set spinner thickness
                )
            }

            // --- Optional: Debug Info ---
            // You can add your DebugText composable here if needed
            // DebugText(scrollOffset.value, totalChartWidthPx, viewPortSize.width, maxScrollOffset, dataPoints.size)

        } // End of else (dataPoints not empty)
    } // End of Box
}