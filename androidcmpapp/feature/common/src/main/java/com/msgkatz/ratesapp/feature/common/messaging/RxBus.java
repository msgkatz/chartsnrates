package com.msgkatz.ratesapp.feature.common.messaging;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;

/**
 * Created by msgkatz on 23/05/17.
 *
 * Lightweight queue to communicate between UI layer parts
 */
public final class RxBus implements IRxBus {

    private final Relay<Object> _bus = PublishRelay.create().toSerialized();

    public void send(Object event) {
        _bus.accept(event);
    }

    public Observable<Object> toObservable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}