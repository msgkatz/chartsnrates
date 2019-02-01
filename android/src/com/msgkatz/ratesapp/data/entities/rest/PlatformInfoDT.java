package com.msgkatz.ratesapp.data.entities.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

/**
 * Created by msgkatz on 22/07/2018.
 */

@Generated("org.jsonschema2pojo")
public class PlatformInfoDT {

    @SerializedName("timezone")
    @Expose
    private String timeZone;

    @SerializedName("serverTime")
    @Expose
    private long serverTime;

    @SerializedName("symbols")
    @Expose
    private ArrayList<ToolDT> toolList;

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("timeZone=");
        sb.append(timeZone);
        sb.append(", serverTime=");
        sb.append(serverTime);
        sb.append(", symbols:");
        for (ToolDT t : toolList)
        {
            sb.append("{");
            sb.append(t.toString());
            sb.append("}");
        }
        return sb.toString();
    }

    public ArrayList<ToolDT> getToolList() {
        return toolList;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public long getServerTime() {
        return serverTime;
    }
}
