package com.msgkatz.ratesapp.data.repo.datastore;

import com.msgkatz.ratesapp.data.entities.Candle;
import com.msgkatz.ratesapp.data.repo.datastore.holders.ICandleStore;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

import io.reactivex.Flowable;

/**
 * Created by msgkatz on 15/08/2018.
 */
@Deprecated
public class CandleStore implements ICandleStore {

    /** Map of values <String as tool_size, Set of Candles> **/
    private Map<String, ConcurrentSkipListSet<Candle>> holdMap = new HashMap<>();
    private ConcurrentSkipListSet<Candle> candles = new ConcurrentSkipListSet<>();

    @Override
    public Flowable<Optional<Candle>> getData() {
        return null;
    }

    @Override
    public Candle getLast() {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public void add(Candle candle) {

    }


}
