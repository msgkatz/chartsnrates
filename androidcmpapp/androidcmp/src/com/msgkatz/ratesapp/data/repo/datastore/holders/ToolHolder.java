package com.msgkatz.ratesapp.data.repo.datastore.holders;

import com.msgkatz.ratesapp.data.entities.UpdateResult;
import com.msgkatz.ratesapp.data.entities.mappers.ToolDataMapper;
import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.data.entities.rest.PlatformInfoDT;
import com.msgkatz.ratesapp.data.entities.rest.ToolDT;
import com.msgkatz.ratesapp.data.net.rest.BinanceRestApi;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.domain.entities.PlatformInfo;
import com.msgkatz.ratesapp.domain.entities.Tool;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

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
    private PlatformInfo platformInfo;
    /*********/
    private Map<String, Tool> toolMap;
    private Set<Asset> quoteAssetSet;
    private Map<String, Asset> quoteAssetMap;

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

    public Flowable<Optional<PlatformInfo>> getPlatformInfo() {
        if (getIsEmpty())
        //if (platformInfo == null)
        {
            return update()
                    .map(new Function<Optional<UpdateResult>, Optional<PlatformInfo>>() {
                        @Override
                        public Optional<PlatformInfo> apply(Optional<UpdateResult> updateResultOptional) throws Exception {
                            if (!updateResultOptional.isEmpty() && updateResultOptional.get().isOk())
                            {
                                return new Optional<PlatformInfo>(platformInfo);
                            }
                            else
                            {
                                return new Optional<PlatformInfo>(null);
                            }
                        }
                    });
        }
        else {
            return Flowable.just(new Optional<PlatformInfo>(platformInfo));
        }
    }

    public Flowable<Optional<Map<String, Tool>>> getToolMap() {
        if (getIsEmpty())
        //if (toolMap == null)
        {
            return update()
                    .map(new Function<Optional<UpdateResult>, Optional<Map<String, Tool>>>() {
                        @Override
                        public Optional<Map<String, Tool>> apply(Optional<UpdateResult> updateResultOptional) throws Exception {
                            if (!updateResultOptional.isEmpty() && updateResultOptional.get().isOk())
                            {
                                return new Optional<Map<String, Tool>>(toolMap);
                            }
                            else
                            {
                                return new Optional<Map<String, Tool>>(null);
                            }
                        }
                    });
        }
        else {
            return Flowable.just(new Optional<Map<String, Tool>>(toolMap));
        }
    }

    public Flowable<Optional<Set<Asset>>> getQuoteAssetSet() {
        if (getIsEmpty())
        //if (quoteAssetSet == null)
        {
            return update()
                    .map(new Function<Optional<UpdateResult>, Optional<Set<Asset>>>() {
                        @Override
                        public Optional<Set<Asset>> apply(Optional<UpdateResult> updateResultOptional) throws Exception {
                            if (!updateResultOptional.isEmpty() && updateResultOptional.get().isOk())
                            {
                                return new Optional<Set<Asset>>(quoteAssetSet);
                            }
                            else
                            {
                                return new Optional<Set<Asset>>(null);
                            }
                        }
                    });
        }
        else {
            return Flowable.just(new Optional<Set<Asset>>(quoteAssetSet));
        }
    }

    public Flowable<Optional<Map<String, Asset>>> getQuoteAssetMap() {
        if (getIsEmpty())
        //if (quoteAssetMap == null)
        {
            return update()
                    .map(new Function<Optional<UpdateResult>, Optional<Map<String, Asset>>>() {
                        @Override
                        public Optional<Map<String, Asset>> apply(Optional<UpdateResult> updateResultOptional) throws Exception {
                            if (!updateResultOptional.isEmpty() && updateResultOptional.get().isOk())
                            {
                                return new Optional<Map<String, Asset>>(quoteAssetMap);
                            }
                            else
                            {
                                return new Optional<Map<String, Asset>>(null);
                            }
                        }
                    });
        }
        else {
            return Flowable.just(new Optional<Map<String, Asset>>(quoteAssetMap));
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
            platformInfo = new PlatformInfo(data.getTimeZone(), data.getServerTime());
            List<ToolDT> toolList = data.getToolList();

            if (toolList != null) {
                toolMap = new HashMap<>();
                quoteAssetSet = new HashSet<>();
                quoteAssetMap = new HashMap<>();
                for (ToolDT item : toolList) {

                    Tool tool = ToolDataMapper.transform(item, innerModel.getAssetMap());
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
