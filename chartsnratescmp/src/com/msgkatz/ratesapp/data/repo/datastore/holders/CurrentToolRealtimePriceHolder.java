package com.msgkatz.ratesapp.data.repo.datastore.holders;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.entities.mappers.CandleDataMapper;
import com.msgkatz.ratesapp.data.entities.mappers.StreamComboDataMapper;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamComboBase;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamEventType;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamKlineEvent;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamMarketTickerMini;
import com.msgkatz.ratesapp.data.net.wsocks.BinanceWSocksApi;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.utils.Logs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by msgkatz on 31/08/2018.
 */

public class CurrentToolRealtimePriceHolder {

    public static final String TAG = CurrentToolRealtimePriceHolder.class.getSimpleName();

    private final BinanceWSocksApi wsApi;

    private final InnerModel innerModel;
    private final Map<String, ConcurrentSkipListSet<Candle>> holdMap; /** Map of values <String as tool_scale, Set of Candles> **/
    private ConcurrentSkipListSet<Candle> candles = new ConcurrentSkipListSet<>();

    public CurrentToolRealtimePriceHolder(BinanceWSocksApi wsApi, InnerModel innerModel)
    {
        this.wsApi = wsApi;
        this.innerModel = innerModel;
        this.holdMap = innerModel.getHoldMap();
    }

    public Observable<Optional<Candle>> subscribeToolPrice(String symbol, String interval)
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
                                candle = candles.last();
                                return new Optional<>(candle);
                            }

                        }

                        return new Optional<>(null);
                    }
                }).toObservable();
    }

    public Observable<Optional<Candle>> subscribeToolPriceCombo(String symbol, String interval)
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
                                candle = candles.last();
                                return new Optional<>(candle);
                            }

                        }

                        return new Optional<>(null);
                    }
                }).toObservable();
    }

    public Observable<Optional<List<Candle>>> getInterimPrices(String symbol, String interval, Long startTime, Long endTime)
    {
        ConcurrentSkipListSet<Candle> set = new ConcurrentSkipListSet<>();
        ConcurrentSkipListSet<Candle> toolPoints = holdMap.get(symbol + "_" + interval);
        if (toolPoints != null) {
            set.addAll(toolPoints);
        }



        TreeSet<Candle> subset = new TreeSet<>();
        if (!set.isEmpty()) {
            Candle fromCandle = new Candle(startTime.longValue() - 1);
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
