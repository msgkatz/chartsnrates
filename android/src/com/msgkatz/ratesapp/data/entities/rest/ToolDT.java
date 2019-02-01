package com.msgkatz.ratesapp.data.entities.rest;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Tools are pairs of assets, like ETHBTC, etc.
 *
 * Created by msgkatz on 22/07/2018.
 */

@Generated("org.jsonschema2pojo")
public class ToolDT //implements Comparable<Tool>
{

    @SerializedName("symbol")
    @Expose
    private String  symbol;

    @SerializedName("baseAsset")
    @Expose
    private String  baseAsset;

    @SerializedName("baseAssetPrecision")
    @Expose
    private int     basePrecision;

    @SerializedName("quoteAsset")
    @Expose
    private String  quoteAsset;

    @SerializedName("quotePrecision")
    @Expose
    private int     quotePrecision;

    @SerializedName("status")
    @Expose
    private String  status;

    @Override
    public String toString() {
        return String.format("symbol:%1$s, baseAsset:%2$s, baseAssetPrecision:%3$s, quoteAsset:%4$s, quoteAssetPrecision:%5$s",
                symbol, baseAsset, Integer.toString(basePrecision),
                quoteAsset, Integer.toString(quotePrecision));
    }

    public String getSymbol() {
        return symbol;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public String getStatus() {
        return status;
    }
}
