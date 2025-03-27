package com.msgkatz.ratesapp.data.network.websocket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

/**
 * WSApi
 */
interface WebSocketDataSource {

    /*******
     * Single streams
     */
    /** <symbol>@kline_<interval> </interval></symbol> */
    //fun getKlineStream(symbol: String, interval: String): Flow<String>
    fun getKlineStream(symbol: String, interval: String): Flow<StreamKlineEventWSModel>

    /** <symbol>@miniTicker </symbol> */
    fun getMiniTickerStream(symbol: String): Flow<String>

    /** !miniTicker@arr  */
    //fun getMiniTickerStreamAll(): Flow<String>
    fun getMiniTickerStreamAll(): Flow<List<StreamMarketTickerMiniWSModel>>

    /** <symbol>@ticker </symbol> */
    fun getTickerStream(symbol: String): Flow<String>

    /** !ticker@arr  */
    fun getTickerStreamAll(): Flow<String>


    /*******
     * Combined streams (comboStreams)
     */
    /** <symbol>@kline_<interval>/<symbol>@miniTicker </symbol></interval></symbol> */
    //fun getKlineAndMiniTickerComboStream(symbol: String, interval: String): Flow<String>
    fun getKlineAndMiniTickerComboStream(symbol: String, interval: String): Flow<StreamComboBaseWSModel>

    fun getKlineAndMiniTickerComboStreamString(symbol: String, interval: String): Flow<String>
}