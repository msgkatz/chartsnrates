package com.msgkatz.ratesapp.data.repo.datastore;

import android.content.Context;

import com.msgkatz.ratesapp.data.entities.rest.Asset;
import com.msgkatz.ratesapp.data.net.rest.BinanceHtmlApi;
import com.msgkatz.ratesapp.data.net.rest.BinanceRestApi;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.data.repo.datastore.holders.AssetHolder;
import com.msgkatz.ratesapp.data.repo.datastore.holders.ToolHolder;
import com.msgkatz.ratesapp.di.app.AppModule;
import com.msgkatz.ratesapp.domain.entities.PlatformInfo;
import com.msgkatz.ratesapp.domain.entities.Tool;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

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
    public AssetToolDataStore(@Named(AppModule.APP_CONTEXT) Context appContext, BinanceRestApi restApi, BinanceHtmlApi htmlApi, InnerModel innerModel)
    {
        this.restApi = restApi;
        this.htmlApi = htmlApi;
        this.assetHolder = new AssetHolder(appContext, htmlApi, innerModel);
        this.toolHolder = new ToolHolder(restApi, innerModel);
    }

    public Observable<Optional<Map<String, Asset>>> getAssetsData()
    {
        return this.assetHolder.getData().toObservable();
    }

    public Observable<Optional<PlatformInfo>> getPlatformInfo()
    {
        return this.toolHolder.getPlatformInfo().toObservable();
    }

    public Observable<Optional<Set<Asset>>> getQuoteAssets()
    {
        return this.toolHolder.getQuoteAssetSet().toObservable();
    }

    public Observable<Optional<Map<String, Asset>>> getQuoteAssetsAsMap()
    {
        return this.toolHolder.getQuoteAssetMap().toObservable();
    }

    public Observable<Optional<Map<String, Tool>>> getToolMap()
    {
        return this.toolHolder.getToolMap().toObservable();
    }
}
