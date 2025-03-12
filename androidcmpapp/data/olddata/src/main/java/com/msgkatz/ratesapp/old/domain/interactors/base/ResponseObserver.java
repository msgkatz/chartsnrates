package com.msgkatz.ratesapp.old.domain.interactors.base;

import com.msgkatz.ratesapp.old.utils.Logs;

import java.util.NoSuchElementException;

/**
 * Created by msgkatz on 19/08/2018.
 */

public abstract class ResponseObserver<E extends Optional<T>, T> extends DefaultObserver<E>
{
    @Override
    public void onNext(E e) {
        if (e.isEmpty())
            onError(new NoSuchElementException("No value present"));
        else
            doNext(e.get());
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable exception) {
        Logs.e(this, exception.toString());
    }

    public abstract void doNext(T t);

}
