package com.msgkatz.ratesapp.data.entities.wsocks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Kline/Candlestick Streamed data (internal part):
 *
 * {
     "e": "kline",     // Event type
     "E": 123456789,   // Event time
     "s": "BNBBTC",    // Symbol
     "k": {
         "t": 123400000, // Kline start time
         "T": 123460000, // Kline close time
         "s": "BNBBTC",  // Symbol
         "i": "1m",      // IntervalDT
         "f": 100,       // First trade ID
         "L": 200,       // Last trade ID
         "o": "0.0010",  // Open price
         "c": "0.0020",  // Close price
         "h": "0.0025",  // High price
         "l": "0.0015",  // Low price
         "v": "1000",    // Base asset volume
         "n": 100,       // Number of trades
         "x": false,     // Is this kline closed?
         "q": "1.0000",  // Quote asset volume
         "V": "500",     // Taker buy base asset volume
         "Q": "0.500",   // Taker buy quote asset volume
         "B": "123456"   // Ignore
     }
    }
 *
 *
 *
 * Created by msgkatz on 15/08/2018.
 */

public class StreamKline {

    @SerializedName("t")
    @Expose
    public long timeStart;

    @SerializedName("T")
    @Expose
    public long timeEnd;

    @SerializedName("s")
    @Expose
    public String symbol;

    @SerializedName("i")
    @Expose
    public String interval;

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
