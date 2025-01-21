package com.msgkatz.ratesapp.old.data.net.wsocks;

import io.reactivex.Flowable;

/**
 * Created by msgkatz on 02/08/2018.
 */

public interface BinanceWSocksApi {

    /*******
     * Single streams
     */

    /** <symbol>@kline_<interval> **/
    Flowable<String> getKlineStream(String symbol, String interval);

    /** <symbol>@miniTicker **/
    Flowable<String> getMiniTickerStream(String symbol);

    /** !miniTicker@arr **/
    Flowable<String> getMiniTickerStreamAll();

    /** <symbol>@ticker **/
    Flowable<String> getTickerStream(String symbol);

    /** !ticker@arr **/
    Flowable<String> getTickerStreamAll();


    /*******
     * Combined streams (comboStreams)
     */

    /** <symbol>@kline_<interval>/<symbol>@miniTicker **/
    Flowable<String> getKlineAndMiniTickerComboStream(String symbol, String interval);
}
