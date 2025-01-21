package com.msgkatz.ratesapp.feature.chartgdx.entities;

//import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.GdxSettings;
import com.msgkatz.ratesapp.old.data.entities.Candle;


import java.util.Locale;

/**
 * Created by msgkatz on 16/09/2018.
 */

public class CandleData {

    private double priceLow;
    private double priceHigh;
    private double priceOpen;
    private double priceClose;
    private long time;
    private int idxByTime;
    private double idxByScaledDensity;
    private double yPriceCloseScaled;
    private double yPriceOpenScaled;
    private double yPriceLowScaled;
    private double yPriceHighScaled;

    public CandleData(Candle candle, int i) {
        this.priceLow = candle.getPriceLow();
        this.priceHigh = candle.getPriceHigh();
        this.priceOpen = candle.getPriceOpen();
        this.priceClose = candle.getPriceClose();
        this.time = ((long)(candle.getTime()/1000));
        this.idxByTime = i;
        this.idxByScaledDensity = (double) (((float) i) * GdxSettings.chartBlockWidthDensityEffective);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),"candleData: O(%1$s),H(%2$s),L(%3$s),C(%4$s),time(%5$s),idx(%6$d),98g(%7$s)",
                Double.toString(getPriceOpen()),Double.toString(getPriceHigh()),Double.toString(getPriceLow()),Double.toString(getPriceClose())
                ,Long.toString(getTime()),getIdxByTime(),Double.toString(getIdxByScaledDensity()));
    }

    public void merge(Candle candle) {
        if (candle.getPriceLow() < this.priceLow) {
            this.priceLow = candle.getPriceLow();
        }
        if (candle.getPriceHigh() > this.priceHigh) {
            this.priceHigh = candle.getPriceHigh();
        }
        this.priceClose = candle.getPriceClose();
    }

    public double getPriceLow() {
        return this.priceLow;
    }

    public double getPriceHigh() {
        return this.priceHigh;
    }

    public double getPriceOpen() {
        return this.priceOpen;
    }

    public double getPriceClose() {
        return this.priceClose;
    }

    public long getTime() {
        return this.time;
    }

    public int getIdxByTime() {
        return this.idxByTime;
    }

    public double getIdxByScaledDensity() {
        return this.idxByScaledDensity;
    }

    public void setIdxByScaledDensity(double d) {
        this.idxByScaledDensity = d;
    }

    public double getYPriceCloseScaled() {
        return this.yPriceCloseScaled;
    }

    public void setYPriceCloseScaled(double d) {
        this.yPriceCloseScaled = d;
    }

    public double getYPriceOpenScaled() {
        return this.yPriceOpenScaled;
    }

    public void setYPriceOpenScaled(double d) {
        this.yPriceOpenScaled = d;
    }

    public double getYPriceLowScaled() {
        return this.yPriceLowScaled;
    }

    public void setYPriceLowScaled(double d) {
        this.yPriceLowScaled = d;
    }

    public double getYPriceHighScaled() {
        return this.yPriceHighScaled;
    }

    public void setYPriceHighScaled(double d) {
        this.yPriceHighScaled = d;
    }
}
