package com.msgkatz.ratesapp.chartdemo.ui.chart2

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

// Data point structure (Unchanged)
@Immutable
data class CryptoDataPoint(
    val timestampMillis: Long,
    val price: Float
)

// Enum to track loading direction
enum class LoadingDirection {
    NONE, PAST, FUTURE
}

// Represents the state of the chart data and loading
data class ChartUiState(
    val dataPoints: List<CryptoDataPoint> = emptyList(),
    val isLoading: Boolean = false, // Single loading flag
    val loadingDirection: LoadingDirection = LoadingDirection.NONE, // Direction of current load
    val canLoadPast: Boolean = true,
    val canLoadFuture: Boolean = false
)

class CryptoChartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChartUiState())
    val uiState: StateFlow<ChartUiState> = _uiState.asStateFlow()

    private var oldestTimestampLoaded: Long? = null
    private var newestTimestampLoaded: Long? = null

    @OptIn(FlowPreview::class)
    private val loadRequests = MutableSharedFlow<LoadingDirection>(replay = 0, extraBufferCapacity = 1)

    init {
        loadInitialData()

        viewModelScope.launch {
            loadRequests
                .debounce(300)
                // Only process if not already loading
                .filter { !_uiState.value.isLoading }
                .collect { direction ->
                    when (direction) {
                        LoadingDirection.PAST -> triggerLoadMorePastData()
                        LoadingDirection.FUTURE -> triggerLoadMoreFutureData()
                        LoadingDirection.NONE -> {} // Should not happen via request
                    }
                }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Use PAST direction for initial load indicator placement
            _uiState.update { it.copy(isLoading = true, loadingDirection = LoadingDirection.PAST) }
            delay(1000)

            val initialEndTime = System.currentTimeMillis()
            // Shorter initial load for easier testing of loading more
            val initialStartTime = initialEndTime - 86400_000L * 3 // Initial load: 3 days
            val initialData = generateDummyData(initialStartTime, initialEndTime, 50)

            oldestTimestampLoaded = initialData.firstOrNull()?.timestampMillis
            newestTimestampLoaded = initialData.lastOrNull()?.timestampMillis

            _uiState.update {
                it.copy(
                    dataPoints = initialData.sortedBy { dp -> dp.timestampMillis },
                    isLoading = false,
                    loadingDirection = LoadingDirection.NONE,
                    canLoadPast = true,
                    canLoadFuture = newestTimestampLoaded?.let { ts -> ts < System.currentTimeMillis() - 60000L } ?: false // Allow future if not up to 'now'
                )
            }
        }
    }

    fun requestLoadMorePastData() {
        // Only request if allowed and not already loading
        if (_uiState.value.canLoadPast && !_uiState.value.isLoading) {
            // Emit PAST direction
            loadRequests.tryEmit(LoadingDirection.PAST)
        }
    }

    fun requestLoadMoreFutureData() {
        // Only request if allowed and not already loading
        if (_uiState.value.canLoadFuture && !_uiState.value.isLoading) {
            // Emit FUTURE direction
            loadRequests.tryEmit(LoadingDirection.FUTURE)
        }
    }

    // --- Private loading logic ---

    private fun triggerLoadMorePastData() {
        viewModelScope.launch {
            val currentOldest = oldestTimestampLoaded ?: return@launch
            // Double check conditions before proceeding
            if (!_uiState.value.canLoadPast || _uiState.value.isLoading) return@launch

            _uiState.update { it.copy(isLoading = true, loadingDirection = LoadingDirection.PAST) }

            println("CHART_VM: Loading past data...")
            delay(1200) // Slightly longer delay to see spinner

            val endTime = currentOldest
            val startTime = endTime - 86400_000L * 7 // Load another 7 days chunk
            val pastData = generateDummyData(startTime, endTime, 100)

            val newOldestTimestamp = pastData.firstOrNull()?.timestampMillis
            oldestTimestampLoaded = newOldestTimestamp ?: currentOldest
            // Add logic here to determine if even more past data is available
            val canLoadMorePast = newOldestTimestamp != null // Simple check: if we loaded data, assume more exists


            _uiState.update { currentState ->
                val combinedData = (pastData + currentState.dataPoints)
                    .distinctBy { it.timestampMillis }
                    .sortedBy { it.timestampMillis }
                currentState.copy(
                    dataPoints = combinedData,
                    isLoading = false,
                    loadingDirection = LoadingDirection.NONE,
                    canLoadPast = canLoadMorePast
                )
            }
            println("CHART_VM: Past data loaded. Points: ${_uiState.value.dataPoints.size}")
        }
    }

    private fun triggerLoadMoreFutureData() {
        viewModelScope.launch {
            val currentNewest = newestTimestampLoaded ?: return@launch
            if (!_uiState.value.canLoadFuture || _uiState.value.isLoading) return@launch

            _uiState.update { it.copy(isLoading = true, loadingDirection = LoadingDirection.FUTURE) }

            println("CHART_VM: Loading future data...")
            delay(1200)

            val startTime = currentNewest
            val endTime = startTime + 86400_000L * 7 // Load next 7 days

            val now = System.currentTimeMillis()
            val canLoadMoreFutureAfterThis: Boolean
            val actualEndTime: Long
            if (endTime >= now - 60000L) { // Load up to near 'now' (1 min buffer)
                actualEndTime = now
                canLoadMoreFutureAfterThis = false // Reached 'now', cannot load more future
            } else {
                actualEndTime = endTime
                canLoadMoreFutureAfterThis = true // More future data might exist later
            }

            val futureData = generateDummyData(startTime, actualEndTime, 100)

            val newNewestTimestamp = futureData.lastOrNull()?.timestampMillis
            newestTimestampLoaded = newNewestTimestamp ?: currentNewest

            _uiState.update { currentState ->
                val combinedData = (currentState.dataPoints + futureData)
                    .distinctBy { it.timestampMillis }
                    .sortedBy { it.timestampMillis }
                currentState.copy(
                    dataPoints = combinedData,
                    isLoading = false,
                    loadingDirection = LoadingDirection.NONE,
                    canLoadFuture = canLoadMoreFutureAfterThis
                )
            }
            println("CHART_VM: Future data loaded. Points: ${_uiState.value.dataPoints.size}")
        }
    }

    // Dummy Data Generation (Unchanged)
    private fun generateDummyData(
        startTimeMillis: Long,
        endTimeMillis: Long,
        count: Int
    ): List<CryptoDataPoint> {
        if (startTimeMillis >= endTimeMillis) return emptyList()
        val timeStep = (endTimeMillis - startTimeMillis) / count.coerceAtLeast(1)
        // Use start price based on adjacent data if possible
        var lastPrice = (if (startTimeMillis == oldestTimestampLoaded) {
            _uiState.value.dataPoints.firstOrNull()?.price
        } else if (endTimeMillis == newestTimestampLoaded){
            _uiState.value.dataPoints.lastOrNull()?.price
        } else { null }) ?: 50000f // Default start

        return List(count) { i ->
            val timestamp = startTimeMillis + i * timeStep
            val priceChange = (Random.nextFloat() - 0.48f) * (lastPrice * 0.02f) // % based fluctuation
            lastPrice = (lastPrice + priceChange).coerceIn(1000f, 100000f)
            CryptoDataPoint(timestamp, lastPrice)
        }.distinctBy { it.timestampMillis }
            .sortedBy { it.timestampMillis }
    }
}