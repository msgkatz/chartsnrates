package com.msgkatz.ratesapp.data.entities.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by msgkatz on 22/07/2018.
 */

@Generated("org.jsonschema2pojo")
public class PriceSimpleDT {

    @SerializedName("symbol")
    @Expose
    public String instrumentSymbol;

    @SerializedName("price")
    @Expose
    public double price;

    public PriceSimpleDT(String symbol, double price)
    {
        this.instrumentSymbol = symbol;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getSymbol() {
        return instrumentSymbol;
    }

    @Override
    public String toString() {
        return String.format("symbol:%1$s, price:%2$s; ",
                instrumentSymbol,
                Double.toString(price));
    }
}
