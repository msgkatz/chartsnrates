package com.msgkatz.ratesapp.domain.interactors.params;

public class IntervalParams extends CommonParams {

    private String interval;

    public IntervalParams(String interval)
    {
        this.interval = interval;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
