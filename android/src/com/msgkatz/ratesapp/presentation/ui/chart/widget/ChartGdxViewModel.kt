package com.msgkatz.ratesapp.presentation.ui.chart.widget

import android.content.Context
import android.os.Handler
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.msgkatz.ratesapp.data.entities.Candle
import com.msgkatz.ratesapp.di.app.AppModule
import com.msgkatz.ratesapp.domain.interactors.GetCurrentPrice
import com.msgkatz.ratesapp.domain.interactors.GetCurrentPricesInterim
import com.msgkatz.ratesapp.domain.interactors.GetIntervalByName
import com.msgkatz.ratesapp.domain.interactors.GetPriceHistory
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver
import com.msgkatz.ratesapp.presentation.common.messaging.IRxBus
import com.msgkatz.ratesapp.presentation.entities.ToolFormat
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.common.ChartGdxGame
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.prerenderer.Controller
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.prerenderer.PreRenderer
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

class ChartGdxViewModel @Inject constructor(
    @Named(AppModule.APP_CONTEXT)
    private val appContext: Context,
    private val mGetPriceHistory: GetPriceHistory,
    private val mGetCurrentPricesInterim: GetCurrentPricesInterim,
    private val mGetCurrentPrice: GetCurrentPrice,
    private val mGetIntervalByName: GetIntervalByName,
    private val rxBus: IRxBus
): ViewModel() {
    companion object {
        val TAG: String = ChartGdxViewModel::class.java.simpleName
    }

    private val mHandler = Handler()
    private val controller: Controller? = null
    private val toolFormat: ToolFormat? = null
    private val mToolName: String? = null
    private val mToolNameLowerCase: String? = null
    private val mInterval: String? = null
    private val isHistoryProcessed = false

    private val firstCandle: Candle? = null
    private val isFirstCandleSaved = false
    private val currentCandleCount = 0

    private val historicalCandles: ArrayList<Candle>? = null

    private val observerPriceHistoryFirst: ResponseObserver<*, *>? = null
    private val observerPriceHistory: ResponseObserver<*, *>? = null
    private val observerCurrentPrice: ResponseObserver<*, *>? = null
    private val observerCurrentPricesInterim: ResponseObserver<*, *>? = null
    private val observerInterval: ResponseObserver<*, *>? = null
    private val disposables = CompositeDisposable()

    private val prevCandle: Candle? = null

    /**
     * From ChartGdxFragment
     */
    var androidApplicationConfiguration: AndroidApplicationConfiguration? = null
    var chartGdxGame: ChartGdxGame? = null
    private var preRenderer: PreRenderer? = null
    //private val mToolName: String? = null
    private val mToolFormat: ToolFormat? = null
    //private val mInterval: String? = null
    /** End of From ChartGdxFragment */

    init {
        initGdxPart()
    }

    private fun initGdxPart() {
        this.preRenderer = PreRenderer().apply { toolFormat = mToolFormat } //preRenderer.setToolFormat(mToolFormat)

//        this.mChartGdxPresenter.setToolFormat(mToolFormat)
//        this.mChartGdxPresenter.setToolName(mToolName)
//        this.mChartGdxPresenter.setController(preRenderer)
//        this.mChartGdxPresenter.setInterval(mInterval)
        this.chartGdxGame = ChartGdxGame(appContext, preRenderer) // (this.viewModelScope.coroutineContext, this.preRenderer)

        this.androidApplicationConfiguration = AndroidApplicationConfiguration()
            .apply {
                useImmersiveMode = false
                numSamples = 4
                useAccelerometer = false
                useGyroscope = false
                useCompass = false
            }
    }
}