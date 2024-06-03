package com.msgkatz.ratesapp.domain.interactors.params;

import androidx.annotation.NonNull;

import com.msgkatz.ratesapp.utils.Parameters;

/**
 * Created by msgkatz on 23/08/2018.
 */

public class PriceHistoryParams extends CommonParams {

    private String symbol;
    private String interval;
    private Long startTime;
    private Long endTime;
    private Integer limit = 300;

    public PriceHistoryParams(String symbol, String interval, Long startTime, Long endTime)
    {
        this.symbol = symbol;
        this.interval = interval;
        this.startTime = checkTime(startTime);
        this.endTime = checkTime(endTime);
    }

    public PriceHistoryParams(String symbol, String interval, Long startTime, Long endTime, Integer limit)
    {
        this.symbol = symbol;
        this.interval = interval;
        this.startTime = checkTime(startTime);
        this.endTime = checkTime(endTime);
        this.limit = limit;
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
        this.startTime = checkTime(startTime);
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = checkTime(endTime);
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * Historyparams passes time values to REST, which demands time in ms
     */
    private Long checkTime(Long time)
    {
        if (time == null)
            return time;

        if (time.longValue() > Parameters.TIME_CHECK_FOR_MS)
            return time;
        else
            return Long.valueOf(time.longValue() * 1000);

    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PriceHistoryParams:: symbol=");
        sb.append(symbol);
        sb.append(", interval=");
        sb.append(interval);
        sb.append(", startTime=");
        sb.append(startTime);
        sb.append(", endTime=");
        sb.append(endTime);
        sb.append(", limit=");
        sb.append(limit);

        return sb.toString();
    }
}
