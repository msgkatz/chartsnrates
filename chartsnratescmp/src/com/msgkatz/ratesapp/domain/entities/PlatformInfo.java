package com.msgkatz.ratesapp.domain.entities;

/**
 * Created by msgkatz on 29/08/2018.
 */

public class PlatformInfo {

    private String timeZone;
    private long serverTime;

    public PlatformInfo(String timeZone, long serverTime)
    {
        this.timeZone = timeZone;
        this.serverTime = serverTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }
}
