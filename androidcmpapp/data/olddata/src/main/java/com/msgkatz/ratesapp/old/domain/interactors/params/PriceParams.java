package com.msgkatz.ratesapp.old.domain.interactors.params;

/**
 * Created by msgkatz on 23/08/2018.
 */

public class PriceParams extends CommonParams {

    private String symbol;
    private String interval;
    private Long startTime;

    public PriceParams(String symbol, String interval, Long startTime)
    {
        this.symbol = symbol;
        this.interval = interval;
        this.startTime = startTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
