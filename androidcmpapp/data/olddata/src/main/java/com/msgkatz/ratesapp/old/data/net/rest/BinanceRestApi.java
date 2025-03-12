package com.msgkatz.ratesapp.old.data.net.rest;

import com.msgkatz.ratesapp.old.data.entities.rest.PlatformInfoDT;
import com.msgkatz.ratesapp.old.data.entities.rest.PriceByTicker;
import com.msgkatz.ratesapp.old.data.entities.rest.PriceSimpleDT;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Binance REST API endpoints
 *
 * Created by msgkatz on 22/07/2018.
 */

public interface BinanceRestApi {

    /**
     * Mapped JSON endpoints
     */
    @GET("v1/exchangeInfo")
    Flowable<Response<PlatformInfoDT>> getPlatformInfo();

    @GET("v1/time")
    Flowable<Response<Long>> getServerTime();

    @GET("v1/ping")
    Flowable<Response<Void>> getPong();

    @GET("v1/ticker/24hr")
    Flowable<Response<List<PriceByTicker>>> getPriceByTicker();

    @GET("v3/ticker/price")
    Flowable<Response<List<PriceSimpleDT>>> getPriceSimple();


    /**
     * Unmapped JSON endpoints
     */
    @GET("v1/klines")
    Flowable<Response<List<List<String>>>> getPriceByCandle(
            @Query("symbol") String symbol,
            @Query("interval") String interval,
            @Query("startTime") Long startTime,
            @Query("endTime") Long endTime,
            @Query("limit") Integer limit
    );

}
