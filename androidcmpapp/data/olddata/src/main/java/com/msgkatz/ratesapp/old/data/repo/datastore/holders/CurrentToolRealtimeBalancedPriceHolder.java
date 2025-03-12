package com.msgkatz.ratesapp.old.data.repo.datastore.holders;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxrelay2.PublishRelay;
import com.msgkatz.ratesapp.old.data.entities.Candle;
import com.msgkatz.ratesapp.old.data.entities.mappers.CandleDataMapper;
import com.msgkatz.ratesapp.old.data.entities.mappers.StreamComboDataMapper;
import com.msgkatz.ratesapp.old.data.entities.wsocks.StreamComboBase;
import com.msgkatz.ratesapp.old.data.entities.wsocks.StreamEventType;
import com.msgkatz.ratesapp.old.data.entities.wsocks.StreamKlineEvent;
import com.msgkatz.ratesapp.old.data.entities.wsocks.StreamMarketTickerMini;
import com.msgkatz.ratesapp.old.data.net.wsocks.BinanceWSocksApi;
import com.msgkatz.ratesapp.old.data.repo.InnerModel;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.old.utils.Logs;
import com.msgkatz.ratesapp.old.utils.TimeUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by msgkatz on 25/09/2018.
 */

public class CurrentToolRealtimeBalancedPriceHolder {

    public static final String TAG = CurrentToolRealtimeBalancedPriceHolder.class.getSimpleName();

    private final BinanceWSocksApi wsApi;
    private final PublishRelay<Optional<Candle>> relayBase;
    protected final CompositeDisposable disposables;
    private int noObserversCount = 0;

    private final InnerModel innerModel;
    /** Map of values <String as tool_scale, Set of Candles> **/
    private final Map<String, ConcurrentSkipListSet<Candle>> holdMap;
    private ConcurrentSkipListSet<Candle> candles = new ConcurrentSkipListSet<>();
    private CopyOnWriteArrayList<Candle> candlesInternal = new CopyOnWriteArrayList<>();

    public CurrentToolRealtimeBalancedPriceHolder(BinanceWSocksApi wsApi, InnerModel innerModel)
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
        initSchedule(symbol, interval);
        initStream(symbol, interval);
        initControlThread();

        return relayBase;
    }

    private void initSchedule(final String symbol, final String interval)
    {
        disposables.clear();
        Runnable act = new Runnable() {
            @Override
            public void run() {
                candles = holdMap.get(symbol + "_" + interval);
                if (candles != null && !candles.isEmpty())
                {
                    Candle last = candles.last();
                    relayBase.accept(new Optional<Candle>(last));
                    addCandleInternal(last);

                }
            }
        };

        Disposable d = Schedulers.newThread().createWorker().schedulePeriodically(act, 5, (long)1000, TimeUnit.MILLISECONDS);
        disposables.add(d);

    }

    private void initStream(final String symbol, final String interval)
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

    private synchronized List<Candle> getCandlesInternal()
    {
        Logs.d(TAG, "getCandlesInternal() Count = " + candlesInternal.size());
        return candlesInternal;
    }

    private Observable<Optional<Candle>> subscribeToolPriceComboInternal(String symbol, String interval)
    {
        return wsApi.getKlineAndMiniTickerComboStream(symbol, interval)
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

    public Observable<Optional<List<Candle>>> getInterimPrices(String symbol, String interval, Long startTime, Long endTime)
    {
        return Observable.just(new Optional<>(getCandlesInternal()));
    }
}
