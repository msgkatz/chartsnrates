package com.msgkatz.ratesapp.old.data.entities.wsocks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Individual Symbol Mini Ticker Streamed data
 *
 * {
     "e": "24hrMiniTicker",  // Event type
     "E": 123456789,         // Event time
     "s": "BNBBTC",          // Symbol
     "c": "0.0025",          // Current day's close price
     "o": "0.0010",          // Open price
     "h": "0.0025",          // High price
     "l": "0.0010",          // Low price
     "v": "10000",           // Total traded base asset volume
     "q": "18"               // Total traded quote asset volume
    }
 *
 * Created by msgkatz on 15/08/2018.
 */

public class StreamMarketTickerMini {

    @SerializedName("E")
    @Expose
    public long eventTime;

    @SerializedName("s")
    @Expose
    public String symbol;

    @SerializedName("o")
    @Expose
    public double open;

    @SerializedName("c")
    @Expose
    public double close;

    @SerializedName("h")
    @Expose
    public double high;

    @SerializedName("l")
    @Expose
    public double low;
}
