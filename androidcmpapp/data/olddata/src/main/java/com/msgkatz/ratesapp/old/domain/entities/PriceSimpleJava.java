package com.msgkatz.ratesapp.old.domain.entities;

import androidx.annotation.NonNull;


import com.msgkatz.ratesapp.old.utils.NumFormatUtil;

import java.util.Locale;

/**
 * Created by msgkatz on 30/08/2018.
 */

public class PriceSimpleJava implements Comparable<PriceSimpleJava> {

    public ToolJava tool;
    public double price;

    public PriceSimpleJava(ToolJava tool, double price)
    {
        this.tool = tool;
        this.price = price;
    }

    public ToolJava getTool() {
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
    public int compareTo(@NonNull PriceSimpleJava o) {
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

        if (!(obj instanceof PriceSimpleJava)) return false;

        PriceSimpleJava o = (PriceSimpleJava) obj;

        return this.tool.getName()
                .equals(o.getTool().getName());
    }

    /***
     * Methods for composables
     * */
    public String getPriceFormatted() {
        return NumFormatUtil.getFormattedPrice(getPrice());
    }


    public String getPair() {
        return String.format(Locale.getDefault(), "%1$s/%2$s",
                getTool().getBaseAsset().getNameShort(),
                getTool().getQuoteAsset().getNameShort());
    }

    public String getBaseAssetNameLong() {
        return getTool().getBaseAsset().getNameLong();
    }
}
