package com.msgkatz.ratesapp.data.repo;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.entities.PlatformInfo;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.domain.entities.Tool;

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

    private Map<String, Interval> intervalMap  = new ConcurrentHashMap<>();

    private PlatformInfo platformInfo;
    private Map<String, Asset> assetMap;
    private Map<String, Tool> toolMap;
    private Set<Asset> quoteAssetSet;
    private Map<String, Asset> quoteAssetMap;

    private Map<String, Set<PriceSimple>> priceSimpleMultiMap;
    private long lastToolListPriceUpdate = -1;

    private Map<String, ConcurrentSkipListSet<Candle>> holdMap; /** Map of values <String as tool_scale, Set of Candles> **/


    @Inject
    public InnerModel()
    {}

    public Map<String, Interval> getIntervalMap() {
        return intervalMap;
    }

    public PlatformInfo getPlatformInfo() {
        return platformInfo;
    }

    public void setPlatformInfo(PlatformInfo platformInfo) {
        this.platformInfo = platformInfo;
    }

    public Map<String, Asset> getAssetMap() {
        return assetMap;
    }

    public void setAssetMap(Map<String, Asset> assetMap) {
        this.assetMap = assetMap;
    }

    public Map<String, Tool> getToolMap() {
        return toolMap;
    }

    public void setToolMap(Map<String, Tool> toolMap) {
        this.toolMap = toolMap;
    }

    public Set<Asset> getQuoteAssetSet() {
        return quoteAssetSet;
    }

    public void setQuoteAssetSet(Set<Asset> quoteAssetSet) {
        this.quoteAssetSet = quoteAssetSet;
    }

    public Map<String, Asset> getQuoteAssetMap() {
        return quoteAssetMap;
    }

    public void setQuoteAssetMap(Map<String, Asset> quoteAssetMap) {
        this.quoteAssetMap = quoteAssetMap;
    }

    public Map<String, Set<PriceSimple>> getPriceSimpleMultiMap() {
        return priceSimpleMultiMap;
    }

    public void setPriceSimpleMultiMap(Map<String, Set<PriceSimple>> priceSimpleMultiMap) {
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
