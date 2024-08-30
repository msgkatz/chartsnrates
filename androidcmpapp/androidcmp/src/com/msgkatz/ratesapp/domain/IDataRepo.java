package com.msgkatz.ratesapp.domain;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.entities.PlatformInfo;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.domain.entities.Tool;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;


/**
 * Created by msgkatz on 10/08/2018.
 */

public interface IDataRepo {

    /**
     * Assets, Tools, PlatformInfo
     */
    Observable<Optional<Map<String, Asset>>> getAssets();

    Observable<Optional<Set<Asset>>> getQuoteAssets();
    Observable<Optional<Map<String, Asset>>> getQuoteAssetsAsMap();

    Observable<Optional<PlatformInfo>> getPlatformInfo();

    Observable<Optional<Map<String, Tool>>> getToolMap();

    /**
     * Current tool info
     */
    Observable<Optional<List<Candle>>> getPriceHistoryByTool(String symbol, String interval, Long startTime, Long endTime, Integer limit);

    Observable<Optional<List<Candle>>> getCurrentPricesInterimByTool(String symbol, String interval, Long startTime);

    Observable<Optional<Candle>> getCurrentPriceByTool(String symbol, String interval, Long startTime);

    Observable<Optional<Candle>> getCurrentPriceByToolCombo(String symbol, String interval, Long startTime);


    /**
     * ToolList prices
     */
    Observable<Optional<Map<String, Set<PriceSimple>>>> getToolListPrice();

    Observable<Optional<Map<String, Set<PriceSimple>>>> getToolListRealtimePrice();

    Observable<Optional<Map<String, Set<PriceSimple>>>> getCombinedToolListPrice();


    /**
     * Intervals for the chart
     */
    Observable<Optional<List<Interval>>> getIntervalList();
    Observable<Optional<Interval>> getIntervalByName(String interval);
}
