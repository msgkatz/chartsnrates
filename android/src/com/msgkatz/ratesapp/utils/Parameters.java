package com.msgkatz.ratesapp.utils;

import com.msgkatz.ratesapp.domain.entities.Interval;

import java.util.Arrays;
import java.util.List;

import static com.msgkatz.ratesapp.domain.entities.Interval.fromString;

/**
 * Created by msgkatz on 20/07/2018.
 */

public class Parameters {

    public final static String BASE_URL                 = "https://www.binance.com/";
    public final static String BASE_URL_REST            = "https://www.binance.com/api/";
    public final static String BASE_URL_WSOCK           = "wss://stream.binance.com:9443";
    public final static String BASE_URL_WSOCK_RAW       = "/ws/";
    public final static String BASE_URL_WSOCK_COMBINED  = "/stream?streams=";

    public final static String BASE_URL_TO_CHANGE       = "ex.bnbstatic.com/images";
    public final static String BASE_URL_TO_CHANGE_TO    = "www.binance.com/file/resources/img";

    public final static String ACTIVE_TOOL_STATUS       = "TRADING";

    public static final String QUOTE_NUMBER_FORMAT_NO_DEC
                                                        = "#######0";
    public static final String MONEY_NUMBER_FORMAT      = "######.00";
    public static final int DEFAULT_MAX_FRACTION_DIGITS = 8;

    /** When =true enables extra logging and few testing functionality **/
    public static boolean DEBUG                         = true;

    /** when =true uses mock backend responses **/
    public static boolean DEBUG_STUB_WS                 = false;

    /** when =true enables extra gdx logging **/
    public static boolean DEBUG_GDX_RENDERING_LOGGING   = true;

    /** Chart's scaling interval list (binance related) **/
    public final static List<Interval> defaulScaletList
                                                        = Arrays.asList(
                                                        fromString("1s"),
                                                        fromString("1m"),
                                                        //fromString("3m"),
                                                        fromString("5m"),
                                                        fromString("15m"),
                                                        fromString("30m"),
                                                        fromString("1h"),
                                                        fromString("2h"),
                                                        fromString("4h"),
                                                        //fromString("6h"),
                                                        fromString("8h"),
                                                        fromString("12h"),
                                                        fromString("1d"),
                                                        //fromString("3d"),
                                                        fromString("1w"),
                                                        fromString("1M"));

    /** app generates extra websocket connection when limit reaches this number**/
    public final static int WEBSOCK_MESSAGE_LIMIT       = 350;

    /**
     * 1537815600000 -- ms
     * 1537827799 -- s
     * 10000000000 - check for s VS ms
     */
    public final static long TIME_CHECK_FOR_MS          = 10000000000L;
}
