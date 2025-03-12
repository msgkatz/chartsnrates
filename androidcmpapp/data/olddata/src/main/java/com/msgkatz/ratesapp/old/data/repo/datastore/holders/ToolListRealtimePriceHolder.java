package com.msgkatz.ratesapp.old.data.repo.datastore.holders;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msgkatz.ratesapp.old.data.entities.mappers.PriceSimpleDTDataMapper;
import com.msgkatz.ratesapp.old.data.entities.rest.AssetDT;
import com.msgkatz.ratesapp.old.data.entities.rest.PriceSimpleDT;
import com.msgkatz.ratesapp.old.data.entities.wsocks.StreamMarketTickerMini;
import com.msgkatz.ratesapp.old.data.net.wsocks.BinanceWSocksApi;
import com.msgkatz.ratesapp.old.data.repo.InnerModel;
import com.msgkatz.ratesapp.old.domain.entities.PriceSimpleJava;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by msgkatz on 24/08/2018.
 */

public class ToolListRealtimePriceHolder {

    private final BinanceWSocksApi wsApi;
    private final InnerModel innerModel;
    private final List<PriceSimpleDT> lastToolPriceList;

    //private final PublishRelay<Optional<List<PriceSimpleDT>>> relayBase;


    public ToolListRealtimePriceHolder(BinanceWSocksApi wsApi, List<PriceSimpleDT> lastToolPriceList, InnerModel innerModel)
    {
        this.wsApi = wsApi;
        this.lastToolPriceList = lastToolPriceList;
        this.innerModel = innerModel;
        //this.relayBase = PublishRelay.create();
    }

    public Observable<Optional<List<PriceSimpleDT>>> subscribeToolListRealtimePrice()
    {
        return wsApi.getMiniTickerStreamAll()
                .map(new Function<String, Optional<List<PriceSimpleDT>>>() {
                    @Override
                    public Optional<List<PriceSimpleDT>> apply(String s) throws Exception {

                        if (s != null)
                        {
                            Type collectionType = new TypeToken<List<StreamMarketTickerMini>>(){}.getType();
                            List<StreamMarketTickerMini> list = new Gson().fromJson(s, collectionType);

                            List<PriceSimpleDT> tmp = PriceSimpleDTDataMapper.transform(list);

                            if (tmp != null && tmp.size() > 0)
                            {
                                lastToolPriceList.clear();
                                lastToolPriceList.addAll(tmp);
                            }
                            return new Optional<>(tmp);
                        }
                        else
                            return new Optional<>(null);
                    }
                }).toObservable();
    }

    public Observable<Optional<Map<String, Set<PriceSimpleJava>>>> subscribeToolPrices()
    {
        return wsApi.getMiniTickerStreamAll()
                .map(new Function<String, Optional<Map<String, Set<PriceSimpleJava>>>>() {
                    @Override
                    public Optional<Map<String, Set<PriceSimpleJava>>> apply(String s) throws Exception {

                        if (s != null)
                        {
                            Type collectionType = new TypeToken<List<StreamMarketTickerMini>>(){}.getType();
                            List<StreamMarketTickerMini> list = new Gson().fromJson(s, collectionType);

                            List<PriceSimpleDT> tmp = PriceSimpleDTDataMapper.transform(list);

                            if (tmp != null && tmp.size() > 0)
                            {
                                fulfillPrices(tmp);
                            }
                            return new Optional<>(innerModel.getPriceSimpleMultiMap());
                        }
                        else
                            return new Optional<>(innerModel.getPriceSimpleMultiMap());
                    }
                }).toObservable();
    }

    private void fulfillPrices(List<PriceSimpleDT> source)
    {
        com.google.common.base.Function<PriceSimpleDT, PriceSimpleJava> func
                = new com.google.common.base.Function<PriceSimpleDT, PriceSimpleJava>(){
            @Override
            public PriceSimpleJava apply(PriceSimpleDT input) {
                PriceSimpleJava result = new PriceSimpleJava(innerModel.getToolMap().get(input.getSymbol()), input.getPrice());
                return result;
            }
        };

        Collection<PriceSimpleJava> collection = Collections2.transform(source, func);

        Map<String, Set<PriceSimpleJava>> _multimap = innerModel.getPriceSimpleMultiMap();

        if (_multimap == null) {
            _multimap = new ConcurrentHashMap<>(); //HashMap<>();
            innerModel.setPriceSimpleMultiMap(_multimap);
        }

        Iterator<AssetDT> iterator = innerModel.getQuoteAssetSet().iterator();
        while (iterator.hasNext())
        {
            AssetDT item = iterator.next();

            Predicate<PriceSimpleJava> predicate = new Predicate<PriceSimpleJava>() {
                @Override
                public boolean apply(PriceSimpleJava input) {
                    return input.getTool().getQuoteAsset().getNameShort()
                            .equals(item.getNameShort());
                }
            };

            Collection<PriceSimpleJava> result = Collections2.filter(collection, predicate);
            //Set<PriceSimple> set = Sets.newHashSet(result);
            Set<PriceSimpleJava> set = Sets.newConcurrentHashSet(result);


            Set<PriceSimpleJava> targetSet = _multimap.get(item.getNameShort());
            if (targetSet == null)
                continue;

//            targetSet.removeAll(set);
//            targetSet.addAll(set);


            TreeSet<PriceSimpleJava> treeSet = new TreeSet<>(new Comparator<PriceSimpleJava>() {
                @Override
                public int compare(PriceSimpleJava o1, PriceSimpleJava o2) {
                    return o1.compareTo(o2);
                }
            });
            treeSet.addAll(targetSet);
            treeSet.removeAll(set);
            treeSet.addAll(set);

            targetSet.clear();
            targetSet.addAll(treeSet);

        }

        innerModel.setLastToolListPriceUpdate(System.currentTimeMillis());

    }
}
