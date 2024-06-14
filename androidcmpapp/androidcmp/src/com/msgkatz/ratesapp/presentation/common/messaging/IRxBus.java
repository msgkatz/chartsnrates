package com.msgkatz.ratesapp.presentation.common.messaging;

import io.reactivex.Observable;

public interface IRxBus {
    void send(Object event);
    Observable<Object> toObservable();
    boolean hasObservers();
}