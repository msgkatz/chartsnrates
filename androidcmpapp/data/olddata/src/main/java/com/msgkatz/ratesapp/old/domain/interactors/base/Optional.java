package com.msgkatz.ratesapp.old.domain.interactors.base;

import java.util.NoSuchElementException;

import io.reactivex.annotations.Nullable;

/**
 * Helper class to handle Null values inside rx2 Observable
 *
 * Created by msgkatz on 12/15/17.
 */

public class Optional<T> {

    private final T optional;

    public Optional(@Nullable T optional) {
        this.optional = optional;
    }

    public boolean isEmpty() {
        return this.optional == null;
    }

    public T get() {
        if (optional == null) {
            throw new NoSuchElementException("No value present");
        }
        return optional;
    }
}
