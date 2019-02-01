package com.msgkatz.ratesapp.data.repo.datastore.holders;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.entities.mappers.CandleDataMapper;
import com.msgkatz.ratesapp.data.net.rest.BinanceRestApi;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.utils.Parameters;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Created by msgkatz on 23/08/2018.
 */

public class HistoricalPriceHolder {

    private final BinanceRestApi restApi;

    private final InnerModel innerModel;
    private final Map<String, ConcurrentSkipListSet<Candle>> holdMap;
    private ConcurrentSkipListSet<Candle> candles = new ConcurrentSkipListSet<>();

    public HistoricalPriceHolder(BinanceRestApi restApi, InnerModel innerModel)
    {
        this.restApi = restApi;
        this.innerModel = innerModel;
        this.holdMap = innerModel.getHoldMap();
    }

    public Flowable<Optional<List<Candle>>> getData(String symbol, String interval, Long startTime, Long endTime, Integer limit) {

        final Interval _interval = innerModel.getIntervalMap().get(interval);

        return restApi.getPriceByCandle(symbol, _interval.getSymbolApi(), startTime, endTime, limit)
                .map(new Function<Response<List<List<String>>>, Optional<List<Candle>>>() {
                    @Override
                    public Optional<List<Candle>> apply(Response<List<List<String>>> listResponse) throws Exception {
                        if (listResponse.isSuccessful())
                        {
                            //List<Candle> list = CandleDataMapper.transform(listResponse.body());
                            //List<Candle> list = CandleDataMapper.transformWithBorders(listResponse.body(), startTime, endTime);
                            //List<Candle> list = CandleDataMapper.transformWithBordersAndInterval(listResponse.body(), startTime, endTime, interval);


                            List<Candle> list = CandleDataMapper.transformWithBordersAndInterval(listResponse.body(), startTime, endTime, _interval);
                            fulfillHoldMap(symbol, interval, list);

                            return new Optional<>(list);
                        }
                        else
                        {
                            return new Optional<>(null);
                        }
                    }
                });
    }

    private void fulfillHoldMap(String symbol, String interval, List<Candle> list)
    {
        candles = holdMap.get(symbol + "_" + interval);
        if (candles == null)
        {
            candles = new ConcurrentSkipListSet<>();
            holdMap.put(symbol + "_" + interval, candles);
        }

        candles.addAll(list);
    }
}
