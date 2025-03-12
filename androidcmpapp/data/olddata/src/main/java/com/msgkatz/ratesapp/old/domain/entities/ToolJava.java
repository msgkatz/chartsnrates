package com.msgkatz.ratesapp.old.domain.entities;

import androidx.annotation.NonNull;

import com.msgkatz.ratesapp.old.data.entities.rest.AssetDT;

/**
 * Created by msgkatz on 30/08/2018.
 */

public class ToolJava implements Comparable<ToolJava> {

    private String name;
    private AssetDT baseAsset;
    private AssetDT quoteAsset;
    private boolean isActive;

    public ToolJava(String name, AssetDT baseAsset, AssetDT quoteAsset)
    {
        this.name = name;
        this.baseAsset = baseAsset;
        this.quoteAsset = quoteAsset;
        this.isActive = true;
    }

    public ToolJava(String name, AssetDT baseAsset, AssetDT quoteAsset, boolean isActive)
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

    public AssetDT getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(AssetDT baseAsset) {
        this.baseAsset = baseAsset;
    }

    public AssetDT getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(AssetDT quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public int compareTo(@NonNull ToolJava o) {
        return this.baseAsset.getNameShort().compareTo(o.getBaseAsset().getNameShort());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (!(obj instanceof ToolJava)) return false;

        ToolJava o = (ToolJava) obj;

        return this.getName()
                .equals(o.getName());
    }
}
