package com.msgkatz.ratesapp.data.repo;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.entities.rest.AssetDT;
import com.msgkatz.ratesapp.domain.entities.IntervalJava;
import com.msgkatz.ratesapp.domain.entities.PlatformInfoJava;
import com.msgkatz.ratesapp.domain.entities.PriceSimpleJava;
import com.msgkatz.ratesapp.domain.entities.ToolJava;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by msgkatz on 30/08/2018.
 */

@Singleton
public class InnerModel {

    private Map<String, IntervalJava> intervalMap  = new ConcurrentHashMap<>();

    private PlatformInfoJava platformInfo;
    private Map<String, AssetDT> assetMap;
    private Map<String, ToolJava> toolMap;
    private Set<AssetDT> quoteAssetSet;
    private Map<String, AssetDT> quoteAssetMap;

    private Map<String, Set<PriceSimpleJava>> priceSimpleMultiMap;
    private long lastToolListPriceUpdate = -1;

    private Map<String, ConcurrentSkipListSet<Candle>> holdMap; /** Map of values <String as tool_scale, Set of Candles> **/


    @Inject
    public InnerModel()
    {}

    public Map<String, IntervalJava> getIntervalMap() {
        return intervalMap;
    }

    public PlatformInfoJava getPlatformInfo() {
        return platformInfo;
    }

    public void setPlatformInfo(PlatformInfoJava platformInfo) {
        this.platformInfo = platformInfo;
    }

    public Map<String, AssetDT> getAssetMap() {
        return assetMap;
    }

    public void setAssetMap(Map<String, AssetDT> assetMap) {
        this.assetMap = assetMap;
    }

    public Map<String, ToolJava> getToolMap() {
        return toolMap;
    }

    public void setToolMap(Map<String, ToolJava> toolMap) {
        this.toolMap = toolMap;
    }

    public Set<AssetDT> getQuoteAssetSet() {
        return quoteAssetSet;
    }

    public void setQuoteAssetSet(Set<AssetDT> quoteAssetSet) {
        this.quoteAssetSet = quoteAssetSet;
    }

    public Map<String, AssetDT> getQuoteAssetMap() {
        return quoteAssetMap;
    }

    public void setQuoteAssetMap(Map<String, AssetDT> quoteAssetMap) {
        this.quoteAssetMap = quoteAssetMap;
    }

    public Map<String, Set<PriceSimpleJava>> getPriceSimpleMultiMap() {
        return priceSimpleMultiMap;
    }

    public void setPriceSimpleMultiMap(Map<String, Set<PriceSimpleJava>> priceSimpleMultiMap) {
        this.priceSimpleMultiMap = priceSimpleMultiMap;
    }

    public long getLastToolListPriceUpdate() {
        return lastToolListPriceUpdate;
    }

    public void setLastToolListPriceUpdate(long lastToolListPriceUpdate) {
        this.lastToolListPriceUpdate = lastToolListPriceUpdate;
    }

    public Map<String, ConcurrentSkipListSet<Candle>> getHoldMap() {
        return holdMap;
    }

    public void setHoldMap(Map<String, ConcurrentSkipListSet<Candle>> holdMap) {
        this.holdMap = holdMap;
    }
}
