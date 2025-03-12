package com.msgkatz.ratesapp.data.network.rest

/**
 * RestApi
 */
interface RestDataSource {
    /**
     * Binance REST API endpoints
     *
     * Created by msgkatz on 02/10/2024.
     */

    /**
     * @GET("v1/exchangeInfo")
     */
    suspend fun getPlatformInfo(): Result<PlatformInfoDTApiModel>

    /**
     * @GET("v1/time")
     */
    suspend fun getServerTime(): Result<Long>

    /**
     * @GET("v1/ping")
     */
    suspend fun getPong(): Result<Int>

    /**
     * @GET("v1/ticker/24hr")
     */
    suspend fun getPriceByTicker(): Result<PriceByTickerApiModel>

    /**
     * @GET("v3/ticker/price")
     */
    suspend fun getPriceSimple(): Result<List<PriceSimpleDTApiModel>>


    /**
     * @GET("v1/klines")
     */
    suspend fun getPriceByCandle(
        /** @Query("symbol") **/    symbol: String?,
        /** @Query("interval") **/  interval: String?,
        /** @Query("startTime") **/ startTime: Long?,
        /** @Query("endTime") **/   endTime: Long?,
        /** @Query("limit") **/     limit: Int?
    ): Result<List<List<String>>>


    /**
     * Binance HTML-based JSON endpoints
     *
     * Created by msgkatz on 22/07/2018.
     */

    /**
     *
     * @GET("assetWithdraw/getAllAsset.html")
     */
    suspend fun getAssets(): Result<List<AssetApiModel>>
}