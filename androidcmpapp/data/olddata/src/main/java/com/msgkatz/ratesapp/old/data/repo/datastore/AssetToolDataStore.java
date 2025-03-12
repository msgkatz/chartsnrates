package com.msgkatz.ratesapp.old.data.repo.datastore;

import android.content.Context;

import com.msgkatz.ratesapp.old.data.DataParams;
import com.msgkatz.ratesapp.old.data.entities.rest.AssetDT;
import com.msgkatz.ratesapp.old.data.net.rest.BinanceHtmlApi;
import com.msgkatz.ratesapp.old.data.net.rest.BinanceRestApi;
import com.msgkatz.ratesapp.old.data.repo.InnerModel;
import com.msgkatz.ratesapp.old.data.repo.datastore.holders.AssetHolder;
import com.msgkatz.ratesapp.old.data.repo.datastore.holders.ToolHolder;
import com.msgkatz.ratesapp.old.domain.entities.PlatformInfoJava;
import com.msgkatz.ratesapp.old.domain.entities.ToolJava;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 30/08/2018.
 */

@Singleton
public class AssetToolDataStore {

    private final BinanceRestApi restApi;
    private final BinanceHtmlApi htmlApi;
    private final AssetHolder assetHolder;
    private final ToolHolder toolHolder;

    @Inject
    public AssetToolDataStore(@Named(DataParams.APP_CONTEXT) Context appContext, BinanceRestApi restApi, BinanceHtmlApi htmlApi, InnerModel innerModel)
    {
        this.restApi = restApi;
        this.htmlApi = htmlApi;
        this.assetHolder = new AssetHolder(appContext, htmlApi, innerModel);
        this.toolHolder = new ToolHolder(restApi, innerModel);
    }

    public Observable<Optional<Map<String, AssetDT>>> getAssetsData()
    {
        return this.assetHolder.getData().toObservable();
    }

    public Observable<Optional<PlatformInfoJava>> getPlatformInfo()
    {
        return this.toolHolder.getPlatformInfo().toObservable();
    }

    public Observable<Optional<Set<AssetDT>>> getQuoteAssets()
    {
        return this.toolHolder.getQuoteAssetSet().toObservable();
    }

    public Observable<Optional<Map<String, AssetDT>>> getQuoteAssetsAsMap()
    {
        return this.toolHolder.getQuoteAssetMap().toObservable();
    }

    public Observable<Optional<Map<String, ToolJava>>> getToolMap()
    {
        return this.toolHolder.getToolMap().toObservable();
    }
}
