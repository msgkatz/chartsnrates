package com.msgkatz.ratesapp.presentation.ui.chart;

import android.os.Handler;

import com.msgkatz.ratesapp.App;
import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.interactors.GetCurrentPrice;
import com.msgkatz.ratesapp.domain.interactors.GetCurrentPricesInterim;
import com.msgkatz.ratesapp.domain.interactors.GetIntervalByName;
import com.msgkatz.ratesapp.domain.interactors.GetPriceHistory;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver;
import com.msgkatz.ratesapp.domain.interactors.params.IntervalParams;
import com.msgkatz.ratesapp.domain.interactors.params.PriceHistoryParams;
import com.msgkatz.ratesapp.domain.interactors.params.PriceParams;
import com.msgkatz.ratesapp.domain.interactors.params.PriceRealtimeParams;
import com.msgkatz.ratesapp.presentation.common.messaging.IRxBus;
import com.msgkatz.ratesapp.presentation.entities.ToolFormat;
import com.msgkatz.ratesapp.presentation.entities.events.BaseEvent;
import com.msgkatz.ratesapp.presentation.entities.events.NewIntervalEvent;
import com.msgkatz.ratesapp.presentation.entities.events.PriceEvent;
import com.msgkatz.ratesapp.presentation.ui.chart.base.BaseChartGdxPresenter;
import com.msgkatz.ratesapp.presentation.ui.chart.base.BaseChartPresenter;
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.ChartDataCallback;
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.prerenderer.Controller;
import com.msgkatz.ratesapp.utils.Logs;
import com.msgkatz.ratesapp.utils.Parameters;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by msgkatz on 15/09/2018.
 */

public class ChartGdxPresenter extends BaseChartGdxPresenter
{
    private final static String TAG = ChartGdxPresenter.class.getSimpleName();

    @Inject
    GetPriceHistory mGetPriceHistory;

    @Inject
    GetCurrentPricesInterim mGetCurrentPricesInterim;

    @Inject
    GetCurrentPrice mGetCurrentPrice;

    @Inject
    GetIntervalByName mGetIntervalByName;

    @Inject
    IRxBus rxBus;

    private Handler mHandler = new Handler();
    private Controller controller;
    private ToolFormat toolFormat;
    private String mToolName;
    private String mToolNameLowerCase;
    private String mInterval;
    private boolean isHistoryProcessed = false;

    private Candle firstCandle;
    private boolean isFirstCandleSaved = false;
    private int currentCandleCount = 0;

    private ArrayList<Candle> historicalCandles;

    private ResponseObserver observerPriceHistoryFirst;
    private ResponseObserver observerPriceHistory;
    private ResponseObserver observerCurrentPrice;
    private ResponseObserver observerCurrentPricesInterim;
    private ResponseObserver observerInterval;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private Candle prevCandle = null;

    @Inject
    public ChartGdxPresenter()
    {}

    public void setController(Controller controller)
    {
        this.controller = controller;
    }

    public void setToolFormat(ToolFormat toolFormat) {
        this.toolFormat = toolFormat;
    }

    public void setToolName(String toolName) {
        this.mToolName = toolName;
        this.mToolNameLowerCase = toolName.toLowerCase();
    }

    public void setInterval(String interval)
    {
        this.mInterval = interval;
    }

    public void updateInterval(Interval interval)
    {
        this.mInterval = interval.getSymbol();
        isHistoryProcessed = false;
        this.onStop();
        this.onStart();
    }

    public String getToolName() {
        return mToolName;
    }

    @Override
    public void onStart() {
        if (this.controller != null && mGetCurrentPrice != null)
        {
            this.controller.renderLoader(false, "");
            this.controller.setChartDataListener(this);
        }

        isFirstCandleSaved = false;
        currentCandleCount = 0;
        initIntervalAndCurrentPrice(mToolNameLowerCase, mInterval, null);

        initEvents();

        //getRouter().removeExtras();

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

        if (observerPriceHistoryFirst != null)
            observerPriceHistoryFirst.dispose();
        if (observerPriceHistory != null)
            observerPriceHistory.dispose();
        if (observerCurrentPrice != null)
            observerCurrentPrice.dispose();
        if (observerCurrentPricesInterim != null)
            observerCurrentPricesInterim.dispose();
        if (observerInterval != null)
            observerInterval.dispose();
        disposables.clear();

    }

    private void initIntervalAndCurrentPrice(String toolName, String interval, Long startTime) {
        IntervalParams intervalParams = new IntervalParams(interval);

        observerInterval = new ResponseObserver<Optional<Interval>, Interval>() {
            @Override
            public void doNext(Interval _interval) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initCurrentPrice(toolName, _interval, startTime);
                    }
                });
            }
        };

        mGetIntervalByName.execute(observerInterval, intervalParams);
    }

    private void initPriceHistoryFirst(String toolName, Interval interval, Long startTime, Long endTime) {
//        PriceHistoryParams priceHistoryParams =
//                new PriceHistoryParams(tool.getName(), Parameters.defaulScaletList.get(0).getValue(),
//                        null, null);

        PriceHistoryParams priceHistoryParams =
                new PriceHistoryParams(toolName.toUpperCase(), interval.getSymbol(), startTime, endTime);

        observerPriceHistoryFirst = new ResponseObserver<Optional<List<Candle>>, List<Candle>>() {
            @Override
            public void doNext(List<Candle> candles) {

                historicalCandles = new ArrayList<>(candles);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initCurrentPricesInterim(toolName, interval, firstCandle.getTime());
                    }
                });

            }

            @Override
            public void onError(Throwable exception) {
                super.onError(exception);
            }
        };

        mGetPriceHistory.execute(observerPriceHistoryFirst, priceHistoryParams);
    }

    public void initPriceHistory(String toolName, String interval,
                                 Long startTime, Long endTime, String rangeName)
    {

        PriceHistoryParams priceHistoryParams =
                new PriceHistoryParams(toolName.toUpperCase(), interval, null, endTime);

        observerPriceHistory = new ResponseObserver<Optional<List<Candle>>, List<Candle>>() {
            @Override
            public void doNext(List<Candle> candles) {

                historicalCandles = new ArrayList<>(candles);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        controller.renderCandlesHistorical(historicalCandles, rangeName);
                    }
                });

            }

            @Override
            public void onError(Throwable exception) {
                super.onError(exception);
            }
        };

        mGetPriceHistory.execute(observerPriceHistory, priceHistoryParams);
    }

    private void initCurrentPrice(String toolName, Interval interval, Long startTime) {


        //PriceParams priceParams = new PriceParams(toolName, interval.getSymbolApi(), startTime); --> not working for "1s"
        PriceParams priceParams = new PriceParams(toolName, interval.getSymbol(), startTime);

        observerCurrentPrice = new ResponseObserver<Optional<Candle>, Candle>() {
            @Override
            public void doNext(Candle candle) {

                if (currentCandleCount == 1) {
                    if (firstCandle.getTime() != candle.getTime())
                        currentCandleCount++;
                }
                else
                    currentCandleCount++;

                //mChartParentPresenter.setPrice(candle.getPriceClose());
                if (rxBus != null)
                    rxBus.send(new PriceEvent(candle.getPriceClose()));

                if (isHistoryProcessed) //otherwise ignore values
                {
                    //process candle
                    //controller.renderCandle1(candle);

                    /**
                     * if (prevCandle == null)
                     *  render1
                     * else if (prevCandle != null && prevCandle.time != candle.time)
                     *  render1
                     * else if (prevCandle != null && prevCandle.time == candle.time)
                     *  render2
                     */

                    if (prevCandle != null && prevCandle.getTime() != candle.getTime())
                        controller.renderCandle1(candle);
                    else if (prevCandle != null && prevCandle.getTime() == candle.getTime())
                        controller.renderCandle2(candle);
                    else if (prevCandle == null)
                        controller.renderCandle1(candle);

                    prevCandle = candle;



                } else {
                    if (!isFirstCandleSaved) {
                        firstCandle = candle;
                        isFirstCandleSaved = true;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                initPriceHistoryFirst(toolName, interval, null, firstCandle.getTime());
                            }
                        });

                    }
                }

                Logs.d(TAG, "candle=" + candle.toString());

            }
        };

        mGetCurrentPrice.execute(observerCurrentPrice, priceParams);
    }

    private void initCurrentPricesInterim_old(String toolName, Interval interval, Long startTime) {

        if (currentCandleCount < 2) {

            Logs.d(TAG, "currentCandleCount < 2");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initCurrentPricesInterim(toolName, interval, startTime);
                }
            }, 1000);

            return;
        }

        PriceRealtimeParams pricesInterimParams =
                new PriceRealtimeParams(toolName, interval.getSymbolApi(), startTime, null);

        observerCurrentPricesInterim = new ResponseObserver<Optional<List<Candle>>, List<Candle>>() {
            @Override
            public void doNext(List<Candle> candles) {

                controller.renderCandlesInterim(new ArrayList<Candle>(candles), interval, "");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getRouter() != null)
                            getRouter().removeExtras();
                        controller.renderCandlesHistorical(historicalCandles, "");
                        isHistoryProcessed = true;
                    }
                }, 250);



            }
        };

        mGetCurrentPricesInterim.execute(observerCurrentPricesInterim, pricesInterimParams);
    }

    private void initCurrentPricesInterim(String toolName, Interval interval, Long startTime) {

        /**
        if (currentCandleCount < 2) {

            Logs.d(TAG, "currentCandleCount < 2");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initCurrentPricesInterim(toolName, interval, startTime);
                }
            }, 1000);

            return;
        }
         **/

        PriceRealtimeParams pricesInterimParams =
                new PriceRealtimeParams(toolName, interval.getSymbolApi(), startTime, null);

        observerCurrentPricesInterim = new ResponseObserver<Optional<List<Candle>>, List<Candle>>() {
            @Override
            public void doNext(List<Candle> candles) {

                ArrayList<Candle> interimCandles = new ArrayList<>();

                if (candles != null && candles.size() <= 2 && historicalCandles != null && historicalCandles.size() >= 2)
                {
                    Candle cdl_1 = historicalCandles.remove(historicalCandles.size() - 1);
                    Candle cdl_2 = historicalCandles.remove(historicalCandles.size() - 1);
                    interimCandles.add(cdl_2);
                    interimCandles.add(cdl_1);
                }

                interimCandles.addAll(candles);

                controller.renderCandlesInterim(interimCandles, interval, "");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getRouter() != null)
                            getRouter().removeExtras();
                        controller.renderCandlesHistorical(historicalCandles, "");
                        isHistoryProcessed = true;
                    }
                }, 250);



            }
        };

        mGetCurrentPricesInterim.execute(observerCurrentPricesInterim, pricesInterimParams);
    }


    private void initEvents()
    {
        disposables.add(rxBus
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {

                        if (object instanceof BaseEvent) {
                            if (object instanceof NewIntervalEvent)
                            {
                                NewIntervalEvent event = (NewIntervalEvent) object;
                                updateInterval(event.getInterval());
                            }
                        }

                    }
                }));
    }

}
