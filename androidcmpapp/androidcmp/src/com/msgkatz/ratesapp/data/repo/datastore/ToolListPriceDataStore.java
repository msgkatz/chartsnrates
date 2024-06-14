package com.msgkatz.ratesapp.data.repo.datastore;

import com.msgkatz.ratesapp.data.entities.rest.PriceSimpleDT;
import com.msgkatz.ratesapp.data.net.rest.BinanceRestApi;
import com.msgkatz.ratesapp.data.net.wsocks.BinanceWSocksApi;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.data.repo.datastore.holders.ToolListPriceHolder;
import com.msgkatz.ratesapp.data.repo.datastore.holders.ToolListRealtimePriceHolder;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 24/08/2018.
 */

@Singleton
public class ToolListPriceDataStore {

    private final BinanceRestApi restApi;
    private final BinanceWSocksApi wsApi;
    private final List<PriceSimpleDT> lastToolPriceList = new ArrayList<>();
    private ToolListPriceHolder toolListPriceHolder;
    private ToolListRealtimePriceHolder toolListRealtimePriceHolder;

    @Inject
    public ToolListPriceDataStore(BinanceRestApi restApi, BinanceWSocksApi wsApi, InnerModel innerModel)
    {
        this.restApi = restApi;
        this.wsApi = wsApi;
        this.toolListPriceHolder = new ToolListPriceHolder(restApi, lastToolPriceList, innerModel);
        this.toolListRealtimePriceHolder = new ToolListRealtimePriceHolder(wsApi, lastToolPriceList, innerModel);
    }

    public Observable<Optional<Map<String, Set<PriceSimple>>>> getToolPrices()
    {
        return toolListPriceHolder.getToolPrices().toObservable();
    }

    public Observable<Optional<Map<String, Set<PriceSimple>>>> getToolRealtimePrices()
    {
        return toolListRealtimePriceHolder.subscribeToolPrices();
    }

    public Observable<Optional<Map<String, Set<PriceSimple>>>> getCombinedToolListPrice()
    {
        return getToolPrices().concatWith(getToolRealtimePrices());
    }
}
