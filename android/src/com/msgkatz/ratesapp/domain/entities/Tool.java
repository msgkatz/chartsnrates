package com.msgkatz.ratesapp.domain.entities;

import com.msgkatz.ratesapp.data.entities.rest.Asset;

import androidx.annotation.NonNull;

/**
 * Created by msgkatz on 30/08/2018.
 */

public class Tool implements Comparable<Tool> {

    private String name;
    private Asset baseAsset;
    private Asset quoteAsset;
    private boolean isActive;

    public Tool(String name, Asset baseAsset, Asset quoteAsset)
    {
        this.name = name;
        this.baseAsset = baseAsset;
        this.quoteAsset = quoteAsset;
        this.isActive = true;
    }

    public Tool(String name, Asset baseAsset, Asset quoteAsset, boolean isActive)
    {
        this.name = name;
        this.baseAsset = baseAsset;
        this.quoteAsset = quoteAsset;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Asset getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(Asset baseAsset) {
        this.baseAsset = baseAsset;
    }

    public Asset getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(Asset quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public int compareTo(@NonNull Tool o) {
        return this.baseAsset.getNameShort().compareTo(o.getBaseAsset().getNameShort());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (!(obj instanceof Tool)) return false;

        Tool o = (Tool) obj;

        return this.getName()
                .equals(o.getName());
    }
}
