package com.msgkatz.ratesapp.data.entities;

/**
 * Created by msgkatz on 29/08/2018.
 */

public class UpdateResult {

    public boolean isOk;

    public UpdateResult(boolean result)
    {
        this.isOk = result;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }
}
