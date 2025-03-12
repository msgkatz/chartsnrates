package com.msgkatz.ratesapp.old.data.entities.wsocks;

/**
 * Created by msgkatz on 02/08/2018.
 */

public class WSocksStatusType {

    public final static int CONNECT     = 1101;
    public final static int RECONNECT   = 1102;
    public final static int DISCONNECT  = 1103;
    public final static int ERROR       = 1104;

    private volatile int currentType = DISCONNECT;

    public static WSocksStatusType getType(int type)
    {
        return new WSocksStatusType(type);
    }

    WSocksStatusType()
    {
        currentType = DISCONNECT;
    }

    WSocksStatusType(int type)
    {
        currentType = type;
    }

    public int getCurrentType() {
        return currentType;
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null &&
                obj instanceof WSocksStatusType)
        {
            if (((WSocksStatusType) obj).getCurrentType() == currentType)
                return true;

            return false;
        }

        return false;
    }

    public boolean equals(int type)
    {
        if (currentType == type)
            return true;
        else
            return false;
    }

}
