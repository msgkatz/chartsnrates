package com.msgkatz.ratesapp.old.data.repo;

import com.msgkatz.ratesapp.old.data.entities.Candle;
import com.msgkatz.ratesapp.old.data.entities.rest.AssetDT;
import com.msgkatz.ratesapp.old.data.repo.datastore.AssetToolDataStore;
import com.msgkatz.ratesapp.old.data.repo.datastore.CurrentToolPriceDataStore;
import com.msgkatz.ratesapp.old.data.repo.datastore.IntervalListDataStore;
import com.msgkatz.ratesapp.old.data.repo.datastore.ToolListPriceDataStore;
import com.msgkatz.ratesapp.old.domain.IDataRepo;
import com.msgkatz.ratesapp.old.domain.entities.IntervalJava;
import com.msgkatz.ratesapp.old.domain.entities.PlatformInfoJava;
import com.msgkatz.ratesapp.old.domain.entities.PriceSimpleJava;
import com.msgkatz.ratesapp.old.domain.entities.ToolJava;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 10/08/2018.
 */

@Singleton
public class DataRepo implements IDataRepo {

    private final AssetToolDataStore assetToolDataStore;
    private final CurrentToolPriceDataStore currentToolPriceDataStore;
    private final ToolListPriceDataStore toolListPriceDataStore;
    private final IntervalListDataStore intervalListDataStore;


    @Inject
    public DataRepo(AssetToolDataStore assetToolDataStore,
                    CurrentToolPriceDataStore currentToolPriceDataStore,
                    ToolListPriceDataStore toolListPriceDataStore,
                    IntervalListDataStore intervalListDataStore)
    {
        this.assetToolDataStore = assetToolDataStore;
        this.currentToolPriceDataStore = currentToolPriceDataStore;
        this.toolListPriceDataStore = toolListPriceDataStore;
        this.intervalListDataStore = intervalListDataStore;
    }

    /**
     * Data store for Assets, Tools, Platform info
     *
     */
    @Override
    public Observable<Optional<Map<String, AssetDT>>> getAssets() {
        return this.assetToolDataStore.getAssetsData();
    }

    @Override
    public Observable<Optional<Set<AssetDT>>> getQuoteAssets() {
        return this.assetToolDataStore.getQuoteAssets();
    }

    @Override
    public Observable<Optional<Map<String, AssetDT>>> getQuoteAssetsAsMap() {
        return this.assetToolDataStore.getQuoteAssetsAsMap();
    }

    @Override
    public Observable<Optional<PlatformInfoJava>> getPlatformInfo() {
        return this.assetToolDataStore.getPlatformInfo();
    }

    @Override
    public Observable<Optional<Map<String, ToolJava>>> getToolMap() {
        return this.assetToolDataStore.getToolMap();
    }

    /**
     * Data store for prices of current selected tool
     *
     */
    @Override
    public Observable<Optional<List<Candle>>> getPriceHistoryByTool(String symbol, String interval, Long startTime, Long endTime, Integer limit) {
        return currentToolPriceDataStore.getPriceHistoryByTool(symbol, interval, startTime, endTime, limit);
    }

    @Override
    public Observable<Optional<List<Candle>>> getCurrentPricesInterimByTool(String symbol, String interval, Long startTime) {
        return currentToolPriceDataStore.getPricesInterimByTool(symbol, interval, startTime);
    }

    @Override
    public Observable<Optional<Candle>> getCurrentPriceByTool(String symbol, String interval, Long startTime) {
        return currentToolPriceDataStore.getToolRealtimePrice(symbol, interval);
    }

    @Override
    public Observable<Optional<Candle>> getCurrentPriceByToolCombo(String symbol, String interval, Long startTime) {
        return currentToolPriceDataStore.getToolRealtimePriceCombo(symbol, interval);
    }

    /**
     * Data store for prices of Tool's List
     *
     */
    @Override
    public Observable<Optional<Map<String, Set<PriceSimpleJava>>>> getToolListPrice()
    {
        return toolListPriceDataStore.getToolPrices();
    }

    @Override
    public Observable<Optional<Map<String, Set<PriceSimpleJava>>>> getToolListRealtimePrice()
    {
        return toolListPriceDataStore.getToolRealtimePrices();
    }

    @Override
    public Observable<Optional<Map<String, Set<PriceSimpleJava>>>> getCombinedToolListPrice()
    {
        return toolListPriceDataStore.getCombinedToolListPrice();
    }

    /**
     * Intervals for the chart
     */
    @Override
    public Observable<Optional<List<IntervalJava>>> getIntervalList() {
        return intervalListDataStore.getIntervalData();
    }

    @Override
    public Observable<Optional<IntervalJava>> getIntervalByName(String interval) {
        return intervalListDataStore.getIntervalByName(interval);
    }
}
