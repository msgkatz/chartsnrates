package com.msgkatz.ratesapp.data.repo.datastore.holders;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.msgkatz.ratesapp.data.entities.rest.AssetDT;
import com.msgkatz.ratesapp.data.entities.rest.PriceSimpleDT;
import com.msgkatz.ratesapp.data.net.rest.BinanceRestApi;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.domain.entities.PriceSimpleJava;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Created by msgkatz on 24/08/2018.
 */

public class ToolListPriceHolder {

    private final static long CACHED_PERIOD_PARAM_MS = 60*1000;

    private final BinanceRestApi restApi;
    private final InnerModel innerModel;
    private final List<PriceSimpleDT> lastToolPriceList;
    private Map<String, Set<PriceSimpleJava>> multimap;

    public ToolListPriceHolder(BinanceRestApi restApi, List<PriceSimpleDT> lastToolPriceList, InnerModel innerModel)
    {
        this.restApi = restApi;
        this.lastToolPriceList = lastToolPriceList;
        this.innerModel = innerModel;
    }

    public Flowable<Optional<List<PriceSimpleDT>>> getToolListPrice()
    {
        return restApi.getPriceSimple()
                .map(new Function<Response<List<PriceSimpleDT>>, Optional<List<PriceSimpleDT>>>() {
                    @Override
                    public Optional<List<PriceSimpleDT>> apply(Response<List<PriceSimpleDT>> response) throws Exception {

                        if (response.isSuccessful())
                        {

                            lastToolPriceList.clear();
                            lastToolPriceList.addAll(response.body());

                            return new Optional<>(response.body());

                        }
                        else if (lastToolPriceList.size() > 0)
                            return new Optional<>(lastToolPriceList);
                        else
                            return new Optional<>(null);

                    }
                });
    }

    public Flowable<Optional<Map<String, Set<PriceSimpleJava>>>> getToolPrices()
    {
        if (isLastUpdatedRecently())
        {
            return Flowable.just(new Optional<>(multimap));
        }
        else
            return restApi.getPriceSimple()
                    .map(new Function<Response<List<PriceSimpleDT>>, Optional<Map<String, Set<PriceSimpleJava>>>>() {
                        @Override
                        public Optional<Map<String, Set<PriceSimpleJava>>> apply(Response<List<PriceSimpleDT>> response) throws Exception {

                            if (response.isSuccessful())
                            {
                                innerModel.setLastToolListPriceUpdate(System.currentTimeMillis());
                                fulfillPrices(response.body());

                                return new Optional<>(multimap);

                            }
                            else
                                return new Optional<>(multimap);

                        }
                    });
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

        //Map<String, Set<PriceSimple>> _multimap = new ConcurrentHashMap<>(); //HashMap<>();

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

            TreeSet<PriceSimpleJava> treeSet = new TreeSet<>(new Comparator<PriceSimpleJava>() {
                @Override
                public int compare(PriceSimpleJava o1, PriceSimpleJava o2) {
                    return o1.compareTo(o2);
                }
            });
            treeSet.addAll(result);
            ConcurrentSkipListSet<PriceSimpleJava> set = new ConcurrentSkipListSet<PriceSimpleJava>(treeSet);
            //Set<PriceSimple> set = Sets.newConcurrentHashSet(treeSet);

            _multimap.put(item.getNameShort(), set);
        }

        this.multimap = _multimap;
        //innerModel.setPriceSimpleMultiMap(multimap);
    }

    private boolean isLastUpdatedRecently()
    {
        long curTime = System.currentTimeMillis();
        if (curTime - innerModel.getLastToolListPriceUpdate() <= CACHED_PERIOD_PARAM_MS)
            return true;
        else
            return false;

    }
}
