package com.msgkatz.ratesapp.presentation.ui.chart.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msgkatz.ratesapp.domain.entities.Interval
import com.msgkatz.ratesapp.domain.entities.Tool
import com.msgkatz.ratesapp.domain.interactors.GetIntervals
import com.msgkatz.ratesapp.domain.interactors.GetTools
import com.msgkatz.ratesapp.domain.interactors.base.Optional
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver
import com.msgkatz.ratesapp.presentation.common.messaging.IRxBus
import com.msgkatz.ratesapp.presentation.entities.ToolFormat
import com.msgkatz.ratesapp.presentation.entities.events.BaseEvent
import com.msgkatz.ratesapp.presentation.entities.events.NewIntervalEvent
import com.msgkatz.ratesapp.presentation.entities.events.PriceEvent
import com.msgkatz.ratesapp.utils.Logs
import com.msgkatz.ratesapp.utils.Parameters
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChartParentViewModel @Inject constructor(
    //private val mToolName: String,
    private val mGetTools: GetTools,
    private val mGetIntervals: GetIntervals,
    private val rxBus: IRxBus?

) : ViewModel() {
    companion object {
        val TAG: String = ChartParentViewModel::class.java.simpleName
    }

    // UI state exposed to the UI
    private val _chartParentToolUiState = MutableStateFlow<ChartParentToolUIState>(ChartParentToolUIState.Loading)
    val chartParentToolUiState: StateFlow<ChartParentToolUIState> = _chartParentToolUiState

    private val _chartParentPriceUIState = MutableStateFlow<ChartParentPriceUIState>(ChartParentPriceUIState.Loading)
    val chartParentPriceUIState: StateFlow<ChartParentPriceUIState> = _chartParentPriceUIState

    private val _chartParentForGdxUIState = MutableStateFlow<ChartParentForGdxUIState>(ChartParentForGdxUIState.Loading)
    val chartParentForGdxUIState: StateFlow<ChartParentForGdxUIState> = _chartParentForGdxUIState

    //private
    var mToolName: String? = null
    private var mTool: Tool? = null
    private var mIntervals: List<Interval>? = null
    private var lastPrice: Double? = null

    //TODO combine with state
    //private
    var mInterval: Interval = Parameters.defaulScaletList[2]

    private var observerTools: ResponseObserver<Optional<Map<String, Tool>>?, Map<String, Tool>>? = null
    private var observerIntervals: ResponseObserver<Optional<List<Interval>>, List<Interval>>? = null
    private val disposables = CompositeDisposable()

    init {
        //onStart()
    }

    fun onStart() {
        observerTools =
            object : ResponseObserver<Optional<Map<String, Tool>>?, Map<String, Tool>>() {
                override fun doNext(stringToolMap: Map<String, Tool>?) {
                    if (stringToolMap != null) {
                        updateState(
                            tool = stringToolMap[mToolName]
                        )
                    }
                }
            }

        observerIntervals =
            object : ResponseObserver<Optional<List<Interval>>, List<Interval>>() {
                override fun doNext(intervalList: List<Interval>?) {
                    Logs.d(TAG, "getting intervals: $intervalList")
                    if (intervalList != null) {
                        updateState(
                            intervals = intervalList
                        )
                    }
                }

                override fun onError(exception: Throwable) {
                    super.onError(exception)
                }
            }

        if (mTool == null) {
            mGetTools.execute(observerTools, null)
        }

        if (mIntervals == null) {
            mGetIntervals.execute(observerIntervals, null)
        }

        initEvents()
    }

    fun onStop() {
        observerTools?.dispose()
        observerIntervals?.dispose()
        disposables.clear()
    }

    @Synchronized
    private fun updateState(tool: Tool? = null, intervals: List<Interval>? = null) {
        viewModelScope.launch {
            tool?.let { mTool = it }
            intervals?.let { mIntervals = it }

            //mTool.let {
                _chartParentToolUiState.value = ChartParentToolUIState.Data(mTool, mIntervals)
            //}
        }
    }

    private fun setPrice(newPrice: Double) {
        viewModelScope.launch {
            //if (getView() != null) getView().updatePrice(newPrice)
            _chartParentPriceUIState.value = ChartParentPriceUIState.Data(lastPrice, newPrice)
            lastPrice = newPrice
        }
    }

    fun provideToolName(toolName: String?) {
        this.mToolName = toolName
        onStart()
    }

    fun setToolNamePrice(toolName: String?, toolPrice: Double?) {

        viewModelScope.launch {
            //this.
            mToolName = toolName
            //lastPrice = toolPrice
            toolPrice?.let { setPrice(it) }
            _chartParentForGdxUIState.value = ChartParentForGdxUIState.Data(
                toolName = mToolName,
                toolFormat = getToolFormat(),
                interval = mInterval
            )
        }
        //onStart()
    }

    fun setConfigurationChange(isLandscape: Boolean)
    {
        Logs.e(TAG, "setConfigurationChange isLandscape=" + isLandscape)
        //make conf change state
    }

    fun provideNewInterval(interval: Interval?) {
        interval?.let {
            mInterval = it
            rxBus?.send(NewIntervalEvent(it))
        }
    }

    fun getToolFormat() = ToolFormat(8, (if (lastPrice == null) 0.0 else lastPrice!!))

    private fun initEvents() {
        rxBus?.let {
            disposables.add(it
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { `object` ->
                    if (`object` is BaseEvent) {
                        if (`object` is PriceEvent) {
                            setPrice(`object`.price)
                        }
                    }
                })
        }
    }


}

sealed interface ChartParentToolUIState {
    data object Loading : ChartParentToolUIState
    data object Empty : ChartParentToolUIState
    data class Data(val tool: Tool?, val intervals: List<Interval>?) : ChartParentToolUIState
}

sealed interface ChartParentPriceUIState {
    data object Loading : ChartParentPriceUIState
    data object Empty : ChartParentPriceUIState
    data class Data(val prevPrice: Double?, val newPrice: Double) : ChartParentPriceUIState
}

sealed interface ChartParentForGdxUIState {
    data object Loading : ChartParentForGdxUIState
    data object Empty : ChartParentForGdxUIState
    data class Data(val toolName: String?, val toolFormat: ToolFormat, val interval: Interval) : ChartParentForGdxUIState
}