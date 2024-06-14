package com.msgkatz.ratesapp.data.entities.wsocks;

/**
 * Individual Symbol Ticker Streamed data
 *
 * {
     "e": "24hrTicker",  // Event type
     "E": 123456789,     // Event time
     "s": "BNBBTC",      // Symbol
     "p": "0.0015",      // Price change
     "P": "250.00",      // Price change percent
     "w": "0.0018",      // Weighted average price
     "x": "0.0009",      // Previous day's close price
     "c": "0.0025",      // Current day's close price
     "Q": "10",          // Close trade's quantity
     "b": "0.0024",      // Best bid price
     "B": "10",          // Best bid quantity
     "a": "0.0026",      // Best ask price
     "A": "100",         // Best ask quantity
     "o": "0.0010",      // Open price
     "h": "0.0025",      // High price
     "l": "0.0010",      // Low price
     "v": "10000",       // Total traded base asset volume
     "q": "18",          // Total traded quote asset volume
     "O": 0,             // Statistics open time
     "C": 86400000,      // Statistics close time
     "F": 0,             // First trade ID
     "L": 18150,         // Last trade Id
     "n": 18151          // Total number of trades
    }
 *
 * Created by msgkatz on 15/08/2018.
 */

public class StreamMarketTicker extends StreamMarketTickerMini {

//    @SerializedName("E")
//    @Expose
//    public long eventTime;
//
//    @SerializedName("s")
//    @Expose
//    public String symbol;
//
//    @SerializedName("o")
//    @Expose
//    public double open;
//
//    @SerializedName("c")
//    @Expose
//    public double close;
//
//    @SerializedName("h")
//    @Expose
//    public double high;
//
//    @SerializedName("l")
//    @Expose
//    public double low;

}
