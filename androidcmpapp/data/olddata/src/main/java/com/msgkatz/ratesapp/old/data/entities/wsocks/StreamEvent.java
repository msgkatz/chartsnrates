package com.msgkatz.ratesapp.old.data.entities.wsocks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Base common part of each issued websocket event
 *
 * Created by msgkatz on 15/08/2018.
 */

public class StreamEvent {

    @SerializedName("e")
    @Expose
    public String eventType;

    @SerializedName("E")
    @Expose
    public long eventTime;

    @SerializedName("s")
    @Expose
    public String symbol;

}
