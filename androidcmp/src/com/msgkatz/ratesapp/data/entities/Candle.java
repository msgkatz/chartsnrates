package com.msgkatz.ratesapp.data.entities;


import io.reactivex.annotations.NonNull;

/**
 * Created by msgkatz on 10/08/2018.
 */

public class Candle implements Comparable<Candle> {

    private double priceHigh;
    private double priceLow;
    private double priceOpen;
    private double priceClose;
    private long time;

    public Candle(double o, double high, double low, double c) {
        this.priceOpen = o;
        this.priceClose = c;
        this.priceHigh = high;
        this.priceLow = low;
    }

    public Candle(double o, double high, double low, double c, long time) {
        this.priceOpen = o;
        this.priceClose = c;
        this.priceHigh = high;
        this.priceLow = low;
        this.time = time;
    }

    public Candle(Candle candle, long time) {
        this.priceOpen = candle.getPriceOpen();
        this.priceClose = candle.getPriceClose();
        this.priceHigh = candle.getPriceHigh();
        this.priceLow = candle.getPriceLow();
        this.time = time;
    }

    public Candle(long time) {
        this.priceOpen = 0;
        this.priceClose = 0;
        this.priceHigh = 0;
        this.priceLow = 0;
        this.time = time;
    }

    public void rebase(double newLow, double newHigh, double newClose) {
        if (newLow < this.priceLow) {
            this.priceLow = newLow;
        }
        if (newHigh > this.priceHigh) {
            this.priceHigh = newHigh;
        }
        this.priceClose = newClose;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getPriceHigh() {
        return priceHigh;
    }

    public void setPriceHigh(double priceHigh) {
        this.priceHigh = priceHigh;
    }

    public double getPriceLow() {
        return priceLow;
    }

    public void setPriceLow(double priceLow) {
        this.priceLow = priceLow;
    }

    public double getPriceOpen() {
        return priceOpen;
    }

    public void setPriceOpen(double priceOpen) {
        this.priceOpen = priceOpen;
    }

    public double getPriceClose() {
        return priceClose;
    }

    public void setPriceClose(double priceClose) {
        this.priceClose = priceClose;
    }

    @Override
    public int compareTo(@NonNull Candle o) {
        return ((Long)time).compareTo(o.getTime());
    }

    @Override
    public String toString() {
        return String.format("Candle: o=%1$s; h=%2$s; l=%3$s; c=%4$s; t=%5$s",
                Double.toString(priceOpen),
                Double.toString(priceHigh),
                Double.toString(priceLow),
                Double.toString(priceClose),
                Long.toString(time)
                );
    }
}
