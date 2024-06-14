package com.msgkatz.ratesapp.presentation.ui.chart.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.msgkatz.ratesapp.data.entities.Candle
import com.msgkatz.ratesapp.di.app.AppModule
import com.msgkatz.ratesapp.domain.entities.Interval
import com.msgkatz.ratesapp.domain.interactors.GetCurrentPrice
import com.msgkatz.ratesapp.domain.interactors.GetCurrentPricesInterim
import com.msgkatz.ratesapp.domain.interactors.GetIntervalByName
import com.msgkatz.ratesapp.domain.interactors.GetPriceHistory
import com.msgkatz.ratesapp.domain.interactors.base.Optional
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver
import com.msgkatz.ratesapp.domain.interactors.params.IntervalParams
import com.msgkatz.ratesapp.domain.interactors.params.PriceHistoryParams
import com.msgkatz.ratesapp.domain.interactors.params.PriceParams
import com.msgkatz.ratesapp.domain.interactors.params.PriceRealtimeParams
import com.msgkatz.ratesapp.presentation.common.messaging.IRxBus
import com.msgkatz.ratesapp.presentation.entities.ToolFormat
import com.msgkatz.ratesapp.presentation.entities.events.BaseEvent
import com.msgkatz.ratesapp.presentation.entities.events.NewIntervalEvent
import com.msgkatz.ratesapp.presentation.entities.events.PriceEvent
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.ChartDataCallback
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.common.ChartGdxGame
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.prerenderer.Controller
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.prerenderer.PreRenderer
import com.msgkatz.ratesapp.utils.Logs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Locale
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
): ViewModel(), ChartDataCallback {
    companion object {
        val TAG: String = ChartGdxViewModel::class.java.simpleName
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private var controller: Controller? = null
    //private val toolFormat: ToolFormat? = null
    private var mToolName: String? = null
    private var mToolNameLowerCase: String? = null
    private var mInterval: String? = null
    private var isHistoryProcessed = false

    private var firstCandle: Candle? = null
    private var isFirstCandleSaved = false
    private var currentCandleCount = 0

    private var historicalCandles: ArrayList<Candle>? = null

    private var observerPriceHistoryFirst: ResponseObserver<Optional<List<Candle>>, List<Candle>>? = null
    private var observerPriceHistory: ResponseObserver<Optional<List<Candle>>, List<Candle>>? = null
    private var observerCurrentPrice: ResponseObserver<Optional<Candle>, Candle>? = null
    private var observerCurrentPricesInterim: ResponseObserver<Optional<List<Candle>>, List<Candle>>? = null
    private var observerInterval: ResponseObserver<Optional<Interval>, Interval>? = null //<*, *>? = null
    private val disposables = CompositeDisposable()

    private var prevCandle: Candle? = null

    /**
     * From ChartGdxFragment
     */
    var androidApplicationConfiguration: AndroidApplicationConfiguration? = null
    var chartGdxGame: ChartGdxGame? = null
    private var preRenderer: PreRenderer? = null
    //private val mToolName: String? = null
    private var mToolFormat: ToolFormat? = null
    //private val mInterval: String? = null
    private var paramsAreSet: Boolean = false
    /** End of From ChartGdxFragment */

    init {}

    fun updateParams(toolName: String, toolFormat: ToolFormat, interval: Interval) {
        if (!paramsAreSet) {
            mToolName = toolName
            mToolNameLowerCase = toolName.lowercase(Locale.getDefault())
            mToolFormat = toolFormat
            mInterval = interval.symbol
            paramsAreSet = true
            initGdxPart()
        }
        //onStart()
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
        this.controller = preRenderer
    }

    /** from presenter **/
    fun updateInterval(interval: Interval) {
        this.mInterval = interval.symbol
        isHistoryProcessed = false
        this.onStop()
        this.onStart()
    }

    override fun getToolName(): String {
        return mToolName!!
    }

    fun onStart() {
        if (controller != null && mGetCurrentPrice != null && paramsAreSet) {
            controller?.renderLoader(false, "")
            controller?.setChartDataListener(this)
        }

        isFirstCandleSaved = false
        currentCandleCount = 0
        initIntervalAndCurrentPrice(mToolNameLowerCase!!, mInterval!!, null)

        initEvents()


    }

    fun onResume() {}

    fun onPause() {}

    fun onStop() {
        observerPriceHistoryFirst?.dispose()
        observerPriceHistory?.dispose()
        observerCurrentPrice?.dispose()
        observerCurrentPricesInterim?.dispose()
        observerInterval?.dispose()
        disposables.clear()
    }

    private fun initIntervalAndCurrentPrice(toolName: String, interval: String, startTime: Long?) {
        val intervalParams = IntervalParams(interval)

        observerInterval = object : ResponseObserver<Optional<Interval>, Interval>() {
            override fun doNext(_interval: Interval) {
                mHandler.post { initCurrentPrice(toolName, _interval, startTime) }
            }
        }

        mGetIntervalByName.execute(observerInterval, intervalParams)
    }

    private fun initPriceHistoryFirst(
        toolName: String,
        interval: Interval,
        startTime: Long?,
        endTime: Long
    ) {


        val priceHistoryParams =
            PriceHistoryParams(
                toolName.uppercase(Locale.getDefault()),
                interval.symbol,
                startTime,
                endTime
            )

        observerPriceHistoryFirst =
            object : ResponseObserver<Optional<List<Candle>>, List<Candle>>() {
                override fun doNext(candles: List<Candle>) {
                    historicalCandles = java.util.ArrayList(candles)

                    mHandler.post {
                        initCurrentPricesInterim(
                            toolName,
                            interval,
                            firstCandle!!.time
                        )
                    }
                }

                override fun onError(exception: Throwable) {
                    super.onError(exception)
                }
            }

        mGetPriceHistory.execute(observerPriceHistoryFirst, priceHistoryParams)
    }

    override fun initPriceHistory(
        toolName: String, interval: String?,
        startTime: Long?, endTime: Long?, rangeName: String?
    ) {
        val priceHistoryParams =
            PriceHistoryParams(toolName.uppercase(Locale.getDefault()), interval, null, endTime)

        // Logs.d(TAG, priceHistoryParams.toString());
        observerPriceHistory =
            object : ResponseObserver<Optional<List<Candle>>, List<Candle>>() {
                override fun doNext(candles: List<Candle>) {
                    historicalCandles = java.util.ArrayList(candles)

                    mHandler.post {
                        controller!!.renderCandlesHistorical(
                            historicalCandles,
                            rangeName
                        )
                    }
                }

                override fun onError(exception: Throwable) {
                    super.onError(exception)
                }
            }

        mGetPriceHistory.execute(observerPriceHistory, priceHistoryParams)
    }

    private fun initCurrentPrice(toolName: String, interval: Interval, startTime: Long?) {
        //PriceParams priceParams = new PriceParams(toolName, interval.getSymbolApi(), startTime); --> not working for "1s"


        val priceParams = PriceParams(toolName, interval.symbol, startTime)

        observerCurrentPrice = object : ResponseObserver<Optional<Candle>, Candle>() {
            override fun doNext(candle: Candle) {
                if (currentCandleCount == 1) {
                    if (firstCandle!!.time != candle.time) currentCandleCount++
                } else currentCandleCount++

                //mChartParentPresenter.setPrice(candle.getPriceClose());
                if (rxBus != null) rxBus.send(PriceEvent(candle.priceClose))

                if (isHistoryProcessed) //otherwise ignore values
                {
                    //process candle
                    //controller.renderCandle1(candle);
                    /**
                     * if (prevCandle == null)
                     * render1
                     * else if (prevCandle != null && prevCandle.time != candle.time)
                     * render1
                     * else if (prevCandle != null && prevCandle.time == candle.time)
                     * render2
                     */

                    if (prevCandle != null && prevCandle!!.getTime() != candle.time) controller!!.renderCandle1(
                        candle
                    )
                    else if (prevCandle != null && prevCandle!!.getTime() == candle.time) controller!!.renderCandle2(
                        candle
                    )
                    else if (prevCandle == null) controller!!.renderCandle1(candle)

                    prevCandle = candle
                } else {
                    if (!isFirstCandleSaved) {
                        firstCandle = candle
                        isFirstCandleSaved = true
                        mHandler.post {
                            initPriceHistoryFirst(
                                toolName,
                                interval,
                                null,
                                firstCandle!!.getTime()
                            )
                        }
                    }
                }

                Logs.d(TAG, "candle=$candle")
            }
        }

        mGetCurrentPrice.execute(observerCurrentPrice, priceParams)
    }

//    private fun initCurrentPricesInterim_old(
//        toolName: String,
//        interval: Interval,
//        startTime: Long
//    ) {
//        if (currentCandleCount < 2) {
//            Logs.d(TAG, "currentCandleCount < 2")
//            mHandler.postDelayed({ initCurrentPricesInterim(toolName, interval, startTime) }, 1000)
//
//            return
//        }
//
//        val pricesInterimParams =
//            PriceRealtimeParams(toolName, interval.symbolApi, startTime, null)
//
//        observerCurrentPricesInterim =
//            object : ResponseObserver<Optional<List<Candle>>, List<Candle>>() {
//                override fun doNext(candles: List<Candle>) {
//                    controller!!.renderCandlesInterim(java.util.ArrayList(candles), interval, "")
//                    mHandler.postDelayed({
//                        if (getRouter() != null) getRouter().removeExtras()
//                        controller.renderCandlesHistorical(historicalCandles, "")
//                        isHistoryProcessed = true
//                    }, 250)
//                }
//            }
//
//        mGetCurrentPricesInterim.execute(observerCurrentPricesInterim, pricesInterimParams)
//    }

    private fun initCurrentPricesInterim(toolName: String, interval: Interval, startTime: Long) {
        /**
         * if (currentCandleCount < 2) {
         *
         * Logs.d(TAG, "currentCandleCount < 2");
         * mHandler.postDelayed(new Runnable() {
         * @Override
         * public void run() {
         * initCurrentPricesInterim(toolName, interval, startTime);
         * }
         * }, 1000);
         *
         * return;
         * }
         */

        val pricesInterimParams =
            PriceRealtimeParams(toolName, interval.symbolApi, startTime, null)

        observerCurrentPricesInterim =
            object : ResponseObserver<Optional<List<Candle>>, List<Candle>>() {
                override fun doNext(candles: List<Candle>) {
                    val interimCandles = java.util.ArrayList<Candle>()

                    if (candles != null && candles.size <= 2 && historicalCandles != null && historicalCandles!!.size >= 2) {
                        val cdl_1 = historicalCandles!!.removeAt(historicalCandles!!.size - 1)
                        val cdl_2 = historicalCandles!!.removeAt(historicalCandles!!.size - 1)
                        interimCandles.add(cdl_2)
                        interimCandles.add(cdl_1)
                    }

                    interimCandles.addAll(candles)

                    controller?.renderCandlesInterim(interimCandles, interval, "")
                    mHandler.postDelayed({
                        //if (getRouter() != null) getRouter().removeExtras()
                        controller?.renderCandlesHistorical(historicalCandles, "")
                        isHistoryProcessed = true
                    }, 250)
                }
            }

        mGetCurrentPricesInterim.execute(observerCurrentPricesInterim, pricesInterimParams)
    }


    private fun initEvents() {
        disposables.add(rxBus
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { `object` ->
                if (`object` is BaseEvent) {
                    if (`object` is NewIntervalEvent) {
                        updateInterval(`object`.interval)
                    }
                }
            })
    }
}