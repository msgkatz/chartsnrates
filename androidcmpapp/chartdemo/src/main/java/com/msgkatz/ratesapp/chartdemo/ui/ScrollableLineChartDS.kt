package com.msgkatz.ratesapp.chartdemo.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random


data class DataChunk(
    val id: Int, // Unique identifier for the chunk
    val data: List<Float>, // Data points in this chunk
    val startIndex: Int // Global index of the first data point in this chunk
)

@Composable
fun ScrollableChartWithLazyRow(
    modifier: Modifier = Modifier,
) {
    val chunkSize = 100
    val dataSpacing = 10.dp
    val loadedChunks = remember { mutableStateListOf<DataChunk>() }
    val lazyListState = rememberLazyListState()

    // Load initial chunk
    LaunchedEffect(Unit) {
        loadNextChunk(loadedChunks, chunkSize)
    }

    // Load more chunks on scroll
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty() && loadedChunks.isNotEmpty()) {
                    val lastVisibleIndex = visibleItems.last().index
                    if (lastVisibleIndex >= loadedChunks.lastIndex - 2) {
                        loadNextChunk(loadedChunks, chunkSize)
                    }
                }
            }
    }

    LazyRow(
        state = lazyListState,
        modifier = modifier.fillMaxSize()
    ) {
        items(loadedChunks, key = { it.id }) { chunk ->
            Canvas(
                modifier = Modifier
                    .width((chunkSize * dataSpacing.value).dp)
                    .fillMaxHeight()
            ) {
                // Draw axes
                drawLine(Color.Gray, Offset(0f, size.height), Offset(size.width, size.height))
                drawLine(Color.Gray, Offset(0f, 0f), Offset(0f, size.height))

                // Draw visible data points
                val visibleStartX = -chunk.startIndex * dataSpacing.toPx()
                val visibleEndX = visibleStartX + size.width

                chunk.data.forEachIndexed { index, value ->
                    val globalX = (chunk.startIndex + index) * dataSpacing.toPx()
                    if (globalX >= visibleStartX && globalX <= visibleEndX) {
                        val localX = globalX - visibleStartX
                        val y = size.height - value
                        drawCircle(Color.Blue, center = Offset(localX, y), radius = 4f)
                    }
                }
            }
        }
    }
}

// Simulate dynamic loading
fun loadNextChunk(chunks: MutableList<DataChunk>, chunkSize: Int) {
    val nextChunkId = chunks.size
    val startIndex = nextChunkId * chunkSize
    val newData = List(chunkSize) { Random.nextFloat() * 500 } // Replace with real data
    chunks.add(DataChunk(nextChunkId, newData, startIndex))
}