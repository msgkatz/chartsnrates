package com.msgkatz.ratesapp.data.repo.datastore;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.net.rest.BinanceRestApi;
import com.msgkatz.ratesapp.data.net.wsocks.BinanceWSocksApi;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.data.repo.datastore.holders.CurrentToolRealtimeBalancedPriceHolder;
import com.msgkatz.ratesapp.data.repo.datastore.holders.CurrentToolRealtimeBalancedV2PriceHolder;
import com.msgkatz.ratesapp.data.repo.datastore.holders.CurrentToolRealtimePriceHolder;
import com.msgkatz.ratesapp.data.repo.datastore.holders.HistoricalPriceHolder;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 23/08/2018.
 */

@Singleton
public class CurrentToolPriceDataStore {

    private final BinanceRestApi restApi;
    private final BinanceWSocksApi wsApi;
    private final InnerModel innerModel;
    private HistoricalPriceHolder historicalPriceHolder;
    private CurrentToolRealtimePriceHolder currentToolRealtimePriceHolder;
    private CurrentToolRealtimeBalancedPriceHolder currentToolRealtimeBalancedPriceHolder_type0;
    private CurrentToolRealtimeBalancedV2PriceHolder currentToolRealtimeBalancedPriceHolder_type1;


    /** Map of values <String as tool_scale, Set of Candles> **/
    private Map<String, ConcurrentSkipListSet<Candle>> holdMap = new HashMap<>();

    @Inject
    public CurrentToolPriceDataStore(BinanceRestApi restApi, BinanceWSocksApi wsApi, InnerModel innerModel)
    {
        this.restApi = restApi;
        this.wsApi = wsApi;
        this.innerModel = innerModel;
        this.innerModel.setHoldMap(holdMap);
        this.historicalPriceHolder = new HistoricalPriceHolder(restApi, innerModel);
        this.currentToolRealtimePriceHolder = new CurrentToolRealtimePriceHolder(wsApi, innerModel);
        this.currentToolRealtimeBalancedPriceHolder_type0 = new CurrentToolRealtimeBalancedPriceHolder(wsApi, innerModel);
        this.currentToolRealtimeBalancedPriceHolder_type1 = new CurrentToolRealtimeBalancedV2PriceHolder(wsApi, innerModel);
    }

    public Observable<Optional<List<Candle>>> getPriceHistoryByTool(String symbol, String interval, Long startTime, Long endTime, Integer limit)
    {
        return historicalPriceHolder.getData(symbol, interval, startTime, endTime, limit)
                .toObservable();
    }

    public Observable<Optional<List<Candle>>> getPricesInterimByTool(String symbol, String interval, Long startTime)
    {
        Interval _interval = this.innerModel.getIntervalMap().get(interval);
        if (_interval.getType() == 0)
            return currentToolRealtimeBalancedPriceHolder_type0.getInterimPrices(symbol, interval, startTime, null);
        else
            return currentToolRealtimeBalancedPriceHolder_type1.getInterimPrices(symbol, interval, startTime, null);
        //return currentToolRealtimePriceHolder.getInterimPrices(symbol, interval, startTime, null);
    }

    public Observable<Optional<List<Candle>>> getPricesInterimByTool(String symbol, String interval, Long startTime, Long endTime)
    {
        return currentToolRealtimePriceHolder.getInterimPrices(symbol, interval, startTime, endTime);
    }

    public Observable<Optional<Candle>> getToolRealtimePrice(String symbol, String interval)
    {
        Interval _interval = this.innerModel.getIntervalMap().get(interval);
        if (_interval.getType() == 0)
            return currentToolRealtimeBalancedPriceHolder_type0.subscribeToolPrice(symbol, interval);
        else
            return currentToolRealtimeBalancedPriceHolder_type1.subscribeToolPrice(symbol, interval);
        //return currentToolRealtimePriceHolder.subscribeToolPrice(symbol, interval);
    }

    public Observable<Optional<Candle>> getToolRealtimePriceCombo(String symbol, String interval)
    {
        return currentToolRealtimePriceHolder.subscribeToolPriceCombo(symbol, interval);
    }
}
