package com.msgkatz.ratesapp.old.data.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by msgkatz on 15/08/2018.
 */

public class IntervalDT {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("type")
    @Expose
    public int type;

    @SerializedName("symbol")
    @Expose
    public String symbol;

    @SerializedName("apiSymbol")
    @Expose
    public String symbolApi;

    @SerializedName("perItemDefaultMs")
    @Expose
    public long perItemDefaultMs;

    @SerializedName("perBlockDefaultMs")
    @Expose
    public long perBlockDefaultMs;

    @SerializedName("perItemMinMs")
    @Expose
    public long perItemMinMs;

    @SerializedName("perBlockMinMs")
    @Expose
    public long perBlockMinMs;

    @SerializedName("perItemMaxMs")
    @Expose
    public long perItemMaxMs;

    @SerializedName("perBlockMaxMs")
    @Expose
    public long perBlockMaxMs;

    @SerializedName("inUse")
    @Expose
    public int inUse;
}
