package com.msgkatz.ratesapp.old.domain.interactors.params;

/**
 * Created by msgkatz on 25/09/2018.
 */

public class PriceRealtimeParams extends CommonParams {

    private String symbol;
    private String interval;
    private Long startTime;
    private Long endTime;

    public PriceRealtimeParams(String symbol, String interval, Long startTime, Long endTime)
    {
        this.symbol = symbol;
        this.interval = interval;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}

