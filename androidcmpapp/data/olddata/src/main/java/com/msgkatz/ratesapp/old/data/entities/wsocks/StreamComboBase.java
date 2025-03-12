package com.msgkatz.ratesapp.old.data.entities.wsocks;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by msgkatz on 24/09/2018.
 */

public class StreamComboBase {

    @SerializedName("stream")
    @Expose
    public String streamName;

    @SerializedName("data")
    @Expose
    public JsonObject data;
}
