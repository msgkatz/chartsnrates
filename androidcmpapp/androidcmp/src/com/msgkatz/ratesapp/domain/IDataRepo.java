package com.msgkatz.ratesapp.domain;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.entities.rest.AssetDT;
import com.msgkatz.ratesapp.domain.entities.IntervalJava;
import com.msgkatz.ratesapp.domain.entities.PlatformInfoJava;
import com.msgkatz.ratesapp.domain.entities.PriceSimpleJava;
import com.msgkatz.ratesapp.domain.entities.ToolJava;
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
    Observable<Optional<Map<String, AssetDT>>> getAssets();

    Observable<Optional<Set<AssetDT>>> getQuoteAssets();
    Observable<Optional<Map<String, AssetDT>>> getQuoteAssetsAsMap();

    Observable<Optional<PlatformInfoJava>> getPlatformInfo();

    Observable<Optional<Map<String, ToolJava>>> getToolMap();

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
    Observable<Optional<Map<String, Set<PriceSimpleJava>>>> getToolListPrice();

    Observable<Optional<Map<String, Set<PriceSimpleJava>>>> getToolListRealtimePrice();

    Observable<Optional<Map<String, Set<PriceSimpleJava>>>> getCombinedToolListPrice();


    /**
     * Intervals for the chart
     */
    Observable<Optional<List<IntervalJava>>> getIntervalList();
    Observable<Optional<IntervalJava>> getIntervalByName(String interval);
}
