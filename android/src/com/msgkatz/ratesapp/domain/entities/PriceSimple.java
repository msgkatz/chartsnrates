package com.msgkatz.ratesapp.domain.entities;

import android.support.annotation.NonNull;

/**
 * Created by msgkatz on 30/08/2018.
 */

public class PriceSimple implements Comparable<PriceSimple> {

    public Tool tool;
    public double price;

    public PriceSimple(Tool tool, double price)
    {
        this.tool = tool;
        this.price = price;
    }

    public Tool getTool() {
        return tool;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("symbol:%1$s, price:%2$s; ",
                tool.getName(),
                Double.toString(price));
    }

    @Override
    public int compareTo(@NonNull PriceSimple o) {
        return this.tool.getBaseAsset().getNameShort()
                .compareTo(o.getTool().getBaseAsset().getNameShort());
    }

    @Override
    public int hashCode() {
        return this.tool.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (!(obj instanceof PriceSimple)) return false;

        PriceSimple o = (PriceSimple) obj;

        return this.tool.getName()
                .equals(o.getTool().getName());
    }
}
