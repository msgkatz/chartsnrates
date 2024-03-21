package com.msgkatz.ratesapp.data.repo.datastore.holders;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msgkatz.ratesapp.data.entities.mappers.PriceSimpleDTDataMapper;
import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.data.entities.rest.PriceSimpleDT;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamMarketTickerMini;
import com.msgkatz.ratesapp.data.net.wsocks.BinanceWSocksApi;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.domain.entities.PriceSimple;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

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

    public Observable<Optional<Map<String, Set<PriceSimple>>>> subscribeToolPrices()
    {
        return wsApi.getMiniTickerStreamAll()
                .map(new Function<String, Optional<Map<String, Set<PriceSimple>>>>() {
                    @Override
                    public Optional<Map<String, Set<PriceSimple>>> apply(String s) throws Exception {

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
        com.google.common.base.Function<PriceSimpleDT,PriceSimple> func
                = new com.google.common.base.Function<PriceSimpleDT,PriceSimple>(){
            @Override
            public PriceSimple apply(PriceSimpleDT input) {
                PriceSimple result = new PriceSimple(innerModel.getToolMap().get(input.getSymbol()), input.getPrice());
                return result;
            }
        };

        Collection<PriceSimple> collection = Collections2.transform(source, func);

        Map<String, Set<PriceSimple>> _multimap = innerModel.getPriceSimpleMultiMap();

        if (_multimap == null) {
            _multimap = new ConcurrentHashMap<>(); //HashMap<>();
            innerModel.setPriceSimpleMultiMap(_multimap);
        }

        Iterator<Asset> iterator = innerModel.getQuoteAssetSet().iterator();
        while (iterator.hasNext())
        {
            Asset item = iterator.next();

            Predicate<PriceSimple> predicate = new Predicate<PriceSimple>() {
                @Override
                public boolean apply(PriceSimple input) {
                    return input.getTool().getQuoteAsset().getNameShort()
                            .equals(item.getNameShort());
                }
            };

            Collection<PriceSimple> result = Collections2.filter(collection, predicate);
            //Set<PriceSimple> set = Sets.newHashSet(result);
            Set<PriceSimple> set = Sets.newConcurrentHashSet(result);


            Set<PriceSimple> targetSet = _multimap.get(item.getNameShort());
            if (targetSet == null)
                continue;

//            targetSet.removeAll(set);
//            targetSet.addAll(set);


            TreeSet<PriceSimple> treeSet = new TreeSet<>(new Comparator<PriceSimple>() {
                @Override
                public int compare(PriceSimple o1, PriceSimple o2) {
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
