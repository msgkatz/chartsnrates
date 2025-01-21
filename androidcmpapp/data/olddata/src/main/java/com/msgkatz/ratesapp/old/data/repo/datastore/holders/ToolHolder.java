package com.msgkatz.ratesapp.old.data.repo.datastore.holders;

import com.msgkatz.ratesapp.old.data.entities.UpdateResult;
import com.msgkatz.ratesapp.old.data.entities.mappers.ToolDataMapper;
import com.msgkatz.ratesapp.old.data.entities.rest.AssetDT;
import com.msgkatz.ratesapp.old.data.entities.rest.PlatformInfoDT;
import com.msgkatz.ratesapp.old.data.entities.rest.ToolDT;
import com.msgkatz.ratesapp.old.data.net.rest.BinanceRestApi;
import com.msgkatz.ratesapp.old.data.repo.InnerModel;
import com.msgkatz.ratesapp.old.domain.entities.PlatformInfoJava;
import com.msgkatz.ratesapp.old.domain.entities.ToolJava;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Created by msgkatz on 19/08/2018.
 */

public class ToolHolder {

    private final Object updateLock = new Object();

    private final BinanceRestApi restApi;
    private final InnerModel innerModel;

    private PlatformInfoDT data;
    private PlatformInfoJava platformInfo;
    /*********/
    private Map<String, ToolJava> toolMap;
    private Set<AssetDT> quoteAssetSet;
    private Map<String, AssetDT> quoteAssetMap;

    private boolean isEmpty = true;

    public ToolHolder(BinanceRestApi restApi, InnerModel innerModel)
    {
        this.restApi = restApi;
        this.innerModel = innerModel;
    }

    public Flowable<Optional<UpdateResult>> update() {
        if (data != null) {
            flush();
        }
        return restApi.getPlatformInfo()
                .map(new Function<Response<PlatformInfoDT>, Optional<UpdateResult>>() {

                    @Override
                    public Optional<UpdateResult> apply(Response<PlatformInfoDT> response) throws Exception {
                        if (response.isSuccessful())
                        {
                            data = response.body();

                            UpdateResult result = new UpdateResult(fulfillDataSets(data));
                            return new Optional<UpdateResult>(result);
                        }
                        else
                        {
                            return new Optional<UpdateResult>(new UpdateResult(false));
                        }
                    }
                });

    }

    public Flowable<Optional<PlatformInfoJava>> getPlatformInfo() {
        if (getIsEmpty())
        //if (platformInfo == null)
        {
            return update()
                    .map(new Function<Optional<UpdateResult>, Optional<PlatformInfoJava>>() {
                        @Override
                        public Optional<PlatformInfoJava> apply(Optional<UpdateResult> updateResultOptional) throws Exception {
                            if (!updateResultOptional.isEmpty() && updateResultOptional.get().isOk())
                            {
                                return new Optional<PlatformInfoJava>(platformInfo);
                            }
                            else
                            {
                                return new Optional<PlatformInfoJava>(null);
                            }
                        }
                    });
        }
        else {
            return Flowable.just(new Optional<PlatformInfoJava>(platformInfo));
        }
    }

    public Flowable<Optional<Map<String, ToolJava>>> getToolMap() {
        if (getIsEmpty())
        //if (toolMap == null)
        {
            return update()
                    .map(new Function<Optional<UpdateResult>, Optional<Map<String, ToolJava>>>() {
                        @Override
                        public Optional<Map<String, ToolJava>> apply(Optional<UpdateResult> updateResultOptional) throws Exception {
                            if (!updateResultOptional.isEmpty() && updateResultOptional.get().isOk())
                            {
                                return new Optional<Map<String, ToolJava>>(toolMap);
                            }
                            else
                            {
                                return new Optional<Map<String, ToolJava>>(null);
                            }
                        }
                    });
        }
        else {
            return Flowable.just(new Optional<Map<String, ToolJava>>(toolMap));
        }
    }

    public Flowable<Optional<Set<AssetDT>>> getQuoteAssetSet() {
        if (getIsEmpty())
        //if (quoteAssetSet == null)
        {
            return update()
                    .map(new Function<Optional<UpdateResult>, Optional<Set<AssetDT>>>() {
                        @Override
                        public Optional<Set<AssetDT>> apply(Optional<UpdateResult> updateResultOptional) throws Exception {
                            if (!updateResultOptional.isEmpty() && updateResultOptional.get().isOk())
                            {
                                return new Optional<Set<AssetDT>>(quoteAssetSet);
                            }
                            else
                            {
                                return new Optional<Set<AssetDT>>(null);
                            }
                        }
                    });
        }
        else {
            return Flowable.just(new Optional<Set<AssetDT>>(quoteAssetSet));
        }
    }

    public Flowable<Optional<Map<String, AssetDT>>> getQuoteAssetMap() {
        if (getIsEmpty())
        //if (quoteAssetMap == null)
        {
            return update()
                    .map(new Function<Optional<UpdateResult>, Optional<Map<String, AssetDT>>>() {
                        @Override
                        public Optional<Map<String, AssetDT>> apply(Optional<UpdateResult> updateResultOptional) throws Exception {
                            if (!updateResultOptional.isEmpty() && updateResultOptional.get().isOk())
                            {
                                return new Optional<Map<String, AssetDT>>(quoteAssetMap);
                            }
                            else
                            {
                                return new Optional<Map<String, AssetDT>>(null);
                            }
                        }
                    });
        }
        else {
            return Flowable.just(new Optional<Map<String, AssetDT>>(quoteAssetMap));
        }
    }

    public void flush() {
        synchronized (updateLock) {
            this.isEmpty = true;
            this.data = null;
            this.platformInfo = null;
            this.toolMap = null;
            this.quoteAssetSet = null;
            this.quoteAssetMap = null;
            innerModel.setPlatformInfo(null);
            innerModel.setToolMap(null);
            innerModel.setQuoteAssetSet(null);
            innerModel.setQuoteAssetMap(null);
        }
    }

    /****************/

    private boolean getIsEmpty()
    {
        synchronized (updateLock) {
            return isEmpty;
        }
    }


    private boolean fulfillDataSets(PlatformInfoDT data) {
        synchronized (updateLock) {
            platformInfo = new PlatformInfoJava(data.getTimeZone(), data.getServerTime());
            List<ToolDT> toolList = data.getToolList();

            if (toolList != null) {
                toolMap = new HashMap<>();
                quoteAssetSet = new HashSet<>();
                quoteAssetMap = new HashMap<>();
                for (ToolDT item : toolList) {

                    ToolJava tool = ToolDataMapper.transform(item, innerModel.getAssetMap());
                    toolMap.put(item.getSymbol(), tool);
                    quoteAssetSet.add(tool.getQuoteAsset());
                    quoteAssetMap.put(tool.getQuoteAsset().getNameShort(), tool.getQuoteAsset());

                }

            }

            boolean retVal = (data != null) && (platformInfo != null) && (toolMap != null)
                    && (quoteAssetSet != null) && (quoteAssetMap != null);

            if (retVal) {
                innerModel.setPlatformInfo(platformInfo);
                innerModel.setToolMap(toolMap);
                innerModel.setQuoteAssetSet(quoteAssetSet);
                innerModel.setQuoteAssetMap(quoteAssetMap);

                this.isEmpty = false;
            }

            return retVal;
        }
    }
}
