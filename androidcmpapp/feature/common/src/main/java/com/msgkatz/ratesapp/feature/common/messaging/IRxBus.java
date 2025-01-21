package com.msgkatz.ratesapp.feature.common.messaging;

import io.reactivex.Observable;

public interface IRxBus {
    void send(Object event);
    Observable<Object> toObservable();
    boolean hasObservers();
}