package com.msgkatz.ratesapp.data.entities.wsocks;

/**
 * Created by msgkatz on 24/09/2018.
 */

public enum StreamEventType {

    TYPE_KLINE("kline"),
    TYPE_24_TICKER_MINI("24hrMiniTicker"),
    TYPE_24_TICKER("24hrTicker"),
    TYPE_UNKNOWN("NA");

    private String typeName;

    StreamEventType(String name)
    {
        typeName = name;
    }

    public static StreamEventType getTypeByName(String typeName)
    {
        if (typeName == null)
            return TYPE_UNKNOWN;
        else if (typeName.equals("kline"))
            return TYPE_KLINE;
        else if (typeName.equals("24hrMiniTicker"))
            return TYPE_24_TICKER_MINI;
        else if (typeName.equals("24hrTicker"))
            return TYPE_24_TICKER;
        else
            return TYPE_UNKNOWN;

    }
}
