package com.msgkatz.ratesapp.old.data.net.rest;

import com.msgkatz.ratesapp.old.data.entities.rest.AssetDT;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Binance HTML-based JSON endpoints
 *
 * Created by msgkatz on 22/07/2018.
 */

public interface BinanceHtmlApi {

    @GET("assetWithdraw/getAllAsset.html")
    Flowable<Response<List<AssetDT>>> getAssets();
}
