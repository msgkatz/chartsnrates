package com.msgkatz.ratesapp.data.entities.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by msgkatz on 22/07/2018.
 *
 *
     "symbol": "BNBBTC",
     "priceChange": "-94.99999800",
     "priceChangePercent": "-95.960",
     "weightedAvgPrice": "0.29628482",
     "prevClosePrice": "0.10002000",
     "lastPrice": "4.00000200",
     "lastQty": "200.00000000",
     "bidPrice": "4.00000000",
     "askPrice": "4.00000200",
     "openPrice": "99.00000000",
     "highPrice": "100.00000000",
     "lowPrice": "0.10000000",
     "volume": "8913.30000000",
     "quoteVolume": "15.30000000",
     "openTime": 1499783499040,
     "closeTime": 1499869899040,
     "firstId": 28385,   // First tradeId
     "lastId": 28460,    // Last tradeId
     "count": 76         // Trade count

 */

@Generated("org.jsonschema2pojo")
public class PriceByTicker {

    @SerializedName("symbol")
    @Expose
    private String  symbol;

    @SerializedName("prevClosePrice")
    @Expose
    private double  prevClosePrice;

    @SerializedName("lastPrice")
    @Expose
    private double  lastPrice;

    @SerializedName("openPrice")
    @Expose
    private double  openPrice;

    @SerializedName("highPrice")
    @Expose
    private double  highPrice;

    @SerializedName("lowPrice")
    @Expose
    private double  lowPrice;

    @SerializedName("openTime")
    @Expose
    private long    openTime;

    @SerializedName("closeTime")
    @Expose
    private long    closeTime;

}
