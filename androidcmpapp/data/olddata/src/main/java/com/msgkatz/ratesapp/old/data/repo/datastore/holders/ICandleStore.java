package com.msgkatz.ratesapp.old.data.repo.datastore.holders;

import com.msgkatz.ratesapp.old.data.entities.Candle;
import com.msgkatz.ratesapp.old.data.repo.datastore.holders.base.IHolderBase;

/**
 * Created by msgkatz on 15/08/2018.
 */

public interface ICandleStore extends IHolderBase<Candle> {

    void add(Candle candle);

    //@Deprecated
    Candle getLast();
}
