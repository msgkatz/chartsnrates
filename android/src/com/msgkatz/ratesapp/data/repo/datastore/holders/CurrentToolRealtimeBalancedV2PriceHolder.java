package com.msgkatz.ratesapp.data.repo.datastore.holders;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxrelay2.PublishRelay;
import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.entities.mappers.CandleDataMapper;
import com.msgkatz.ratesapp.data.entities.mappers.StreamComboDataMapper;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamComboBase;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamEventType;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamKlineEvent;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamMarketTickerMini;
import com.msgkatz.ratesapp.data.net.wsocks.BinanceWSocksApi;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.utils.Logs;
import com.msgkatz.ratesapp.utils.TimeUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CurrentToolRealtimeBalancedV2PriceHolder {

    public static final String TAG = CurrentToolRealtimeBalancedV2PriceHolder.class.getSimpleName();

    private final BinanceWSocksApi wsApi;
    private final PublishRelay<Optional<Candle>> relayBase;
    protected final CompositeDisposable disposables;
    private int noObserversCount = 0;

    private final InnerModel innerModel;
    /** Map of values <String as tool_scale, Set of Candles> **/
    private final Map<String, ConcurrentSkipListSet<Candle>> holdMap;
    private ConcurrentSkipListSet<Candle> candles = new ConcurrentSkipListSet<>();
    private CopyOnWriteArrayList<Candle> candlesInternal = new CopyOnWriteArrayList<>();

    public CurrentToolRealtimeBalancedV2PriceHolder(BinanceWSocksApi wsApi, InnerModel innerModel)
    {
        this.wsApi = wsApi;
        this.innerModel = innerModel;
        this.holdMap = innerModel.getHoldMap();
        this.relayBase = PublishRelay.create();
        this.disposables = new CompositeDisposable();
    }

    public Observable<Optional<Candle>> subscribeToolPrice(String symbol, String interval)
    {
        //TODO: start remote candle data accumulation
        candlesInternal.clear();
        Interval _interval = innerModel.getIntervalMap().get(interval);

        cleanContainer(symbol, _interval); // iss-3 implementation
        initSchedule(symbol, _interval);
        initStream(symbol, _interval);
        initControlThread();

        return relayBase;
    }

    private void cleanContainer(final String symbol, final Interval interval) {
        candles = holdMap.get(symbol + "_" + interval.getSymbol());
        if (candles != null && !candles.isEmpty())
        {
            candles.clear();
        }
    }

    long lastTime = -1;
    private void initSchedule(final String symbol, final Interval interval)
    {
        disposables.clear();
        Runnable act = new Runnable() {
            @Override
            public void run() {
                candles = holdMap.get(symbol + "_" + interval.getSymbol());
                if (candles != null && !candles.isEmpty())
                {
                    Candle last = candles.last();

                    if (interval.getType() == 0) {
                        if (lastTime < 0)
                            lastTime = last.getTime();
                        else if (lastTime == last.getTime())
                        {
                            lastTime = lastTime + interval.getPerItemDefaultMs();
                            last = new Candle(last, lastTime);
                            candles.add(last);
                        }
                    }
                    relayBase.accept(new Optional<Candle>(last));

                    //TODO: stupid workaround for "1s" interval - need to refactor -- disabled for tests
//                    if (interval.getId() == 0)
//                        addCandleInternalV2(last);

                }
            }
        };

        Disposable d = Schedulers.newThread().createWorker().schedulePeriodically(act, 5, (long)1000, TimeUnit.MILLISECONDS);
        disposables.add(d);

    }

    private void initStream(final String symbol, final Interval interval)
    {
        Disposable disposable =
                //subscribeToolPriceInternal(symbol, interval)
                subscribeToolPriceComboInternal(symbol, interval)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.newThread())
                        .subscribe();
        disposables.add(disposable);

    }

    private void initControlThread()
    {
        Runnable act = new Runnable() {
            @Override
            public void run() {
                if (!relayBase.hasObservers())
                    noObserversCount++;

                if (noObserversCount > 4)
                {
                    noObserversCount = 0;
                    disposables.clear();
                }
            }
        };

        Disposable d = Schedulers.newThread().createWorker().schedulePeriodically(act, 5, (long)2, TimeUnit.SECONDS);
        disposables.add(d);

    }

    private synchronized void addCandleInternal(Candle candle)
    {
        if (candlesInternal.size() == 0)
        {
            int extraCandlesCount = (int)(((long)(candle.getTime()/1000)) - TimeUtil.normalizeInSeconds(60, ((long)(candle.getTime()/1000))));
            for (int i = 0; i < extraCandlesCount; i++)
                candlesInternal.add(candle);
            Logs.d(TAG, "extraCandlesCount = " + extraCandlesCount);
        }
        candlesInternal.add(candle);
    }

    private synchronized void addCandleInternalV2(Candle candle)
    {

    }

    @Deprecated
    private synchronized List<Candle> getCandlesInternal()
    {
        Logs.d(TAG, "getCandlesInternal() Count = " + candlesInternal.size());
        return candlesInternal;
    }

    private Observable<Optional<Candle>> subscribeToolPriceComboInternal(String symbol, final Interval interval)
    {
        return wsApi.getKlineAndMiniTickerComboStream(symbol, interval.getSymbolApi())
                .map(new Function<String, Optional<Candle>>() {
                    @Override
                    public Optional<Candle> apply(String s) throws Exception {
                        Logs.e(TAG, s);

                        if (s != null)
                        {
                            Candle candle = null;

                            try
                            {
                                //Type type = new TypeToken<StreamComboBase>() {}.getType();
                                //StreamComboBase comboBase = new Gson().fromJson(s, type);

                                StreamComboBase comboBase = new Gson().fromJson(s, StreamComboBase.class);

                                StreamEventType eventType = StreamComboDataMapper.parseEvent(comboBase);

                                if (eventType == StreamEventType.TYPE_KLINE) {


                                    Type typeKline = new TypeToken<StreamKlineEvent>() {}.getType();
                                    String data = "";
                                    if (comboBase.data instanceof JsonObject)
                                        data = ((JsonObject)comboBase.data).toString();
                                    StreamKlineEvent event = new Gson().fromJson(data, typeKline);

                                    candle = CandleDataMapper.transform(event.kline, event.eventTime);
                                }
                                else if (eventType == StreamEventType.TYPE_24_TICKER_MINI) {

                                    Type typeTickerMini = new TypeToken<StreamMarketTickerMini>() {}.getType();
                                    String data = "";
                                    if (comboBase.data instanceof JsonObject)
                                        data = ((JsonObject)comboBase.data).toString();
                                    StreamMarketTickerMini tickerMini = new Gson().fromJson(data, typeTickerMini);

                                    candle = CandleDataMapper.transform(tickerMini);
                                }
                            }
                            catch (Exception ex)
                            {
                                Logs.e(TAG, ex.toString());
                            }

                            candles = holdMap.get(symbol + "_" + interval.getSymbol());
                            if (candles == null)
                            {
                                candles = new ConcurrentSkipListSet<>();
                                holdMap.put(symbol + "_" + interval.getSymbol(), candles);
                            }

                            if (candle != null) {
                                //TODO: rebase last vs. add new
                                if (interval.getType() == 0)
                                {
                                    candles.add(candle);
                                    return new Optional<>(candle);
                                }
                                else
                                {
                                    if (candles.size() == 0) {
                                        candle.setTime(TimeUtil.normalizeInSeconds((interval.getPerItemDefaultMs() / 1000), candle.getTime() / 1000) * 1000);
                                        candles.add(candle);
                                    } else {
                                        if (candle.getTime() < (candles.last().getTime() + interval.getPerItemDefaultMs())) {
                                            candles.last().rebase(candle.getPriceLow(), candle.getPriceHigh(), candle.getPriceClose());
                                            relayBase.accept(new Optional<Candle>(candles.last()));
                                        } else {
                                            candle.setTime(TimeUtil.normalizeInSeconds((interval.getPerItemDefaultMs() / 1000), candle.getTime() / 1000) * 1000);
                                            candles.add(candle);
                                        }
                                    }
                                    return new Optional<>(candle);
                                }
                            }
                            else
                            {
                                if (candles.isEmpty())
                                {
                                    return new Optional<>(null);
                                }
                                else {
                                    candle = candles.last();
                                    return new Optional<>(candle);
                                }
                            }

                        }

                        return new Optional<>(null);
                    }
                }).toObservable();
    }

    private Observable<Optional<Candle>> subscribeToolPriceInternal(String symbol, String interval)
    {
        return wsApi.getKlineStream(symbol, interval)
                .map(new Function<String, Optional<Candle>>() {
                    @Override
                    public Optional<Candle> apply(String s) throws Exception {

                        Logs.e(TAG, s);

                        if (s != null)
                        {
                            Candle candle = null;

                            try
                            {
                                Type type = new TypeToken<StreamKlineEvent>() {}.getType();
                                StreamKlineEvent event = new Gson().fromJson(s, type);

                                candle = CandleDataMapper.transform(event.kline, event.eventTime);
                            }
                            catch (Exception ex)
                            {
                                Logs.e(TAG, ex.toString());
                            }

                            candles = holdMap.get(symbol + "_" + interval);
                            if (candles == null)
                            {
                                candles = new ConcurrentSkipListSet<>();
                                holdMap.put(symbol + "_" + interval, candles);
                            }

                            if (candle != null) {
                                candles.add(candle);
                                return new Optional<>(candle);
                            }
                            else
                            {
                                if (candles.isEmpty())
                                {
                                    return new Optional<>(null);
                                }
                                else {
                                    candle = candles.last();
                                    return new Optional<>(candle);
                                }
                            }

                        }

                        return new Optional<>(null);
                    }
                }).toObservable();
    }

    public Observable<Optional<List<Candle>>> getInterimPricesOld(String symbol, String interval, Long startTime, Long endTime)
    {
        return Observable.just(new Optional<>(getCandlesInternal()));
    }

    public Observable<Optional<List<Candle>>> getInterimPrices(String symbol, String interval, Long startTime, Long endTime)
    {
        Interval _interval = innerModel.getIntervalMap().get(interval);
        ConcurrentSkipListSet<Candle> set = new ConcurrentSkipListSet<>();
        ConcurrentSkipListSet<Candle> toolPoints = holdMap.get(symbol + "_" + _interval.getSymbol());
        if (toolPoints != null) {
            set.addAll(toolPoints);
        }



        TreeSet<Candle> subset = new TreeSet<>();
        if (!set.isEmpty()) {
            //Candle fromCandle = new Candle(startTime.longValue() - 1);
            Candle fromCandle = new Candle(startTime.longValue());
            fromCandle.setTime(TimeUtil.normalizeInSeconds((_interval.getPerItemDefaultMs() / 1000), fromCandle.getTime() / 1000) * 1000 - 1);

            if (endTime == null)
            {
                SortedSet<Candle> found = set.tailSet(fromCandle);
                if (found != null) {
                    subset.addAll(found);
                }
            }
            else
            {
                Candle toCandle = new Candle(endTime.longValue());
                SortedSet<Candle> found = set.subSet(fromCandle, toCandle);
                if (found != null) {
                    subset.addAll(found);
                }
            }
        }

        List<Candle> retList = new ArrayList<>(subset);

        return Observable.just(new Optional<>(retList));
    }
}
