package com.msgkatz.ratesapp.data.repos.base

import com.msgkatz.ratesapp.data.model.Candle

@Deprecated("no need for now")
interface InnerModelRepository {

}
@Deprecated("no need for now")
class InnerModelRepositoryImpl(
    private val map: MutableMap<String, MutableSet<Candle>>,

//    private Map<String, Interval> intervalMap  = new ConcurrentHashMap<>();
//
//private PlatformInfo platformInfo;
//private Map<String, Asset> assetMap;
//private Map<String, Tool> toolMap;
//private Set<Asset> quoteAssetSet;
//private Map<String, Asset> quoteAssetMap;
//
//private Map<String, Set<PriceSimple>> priceSimpleMultiMap;
//private long lastToolListPriceUpdate = -1;
//
//private Map<String, ConcurrentSkipListSet<Candle>> holdMap;
) {
}