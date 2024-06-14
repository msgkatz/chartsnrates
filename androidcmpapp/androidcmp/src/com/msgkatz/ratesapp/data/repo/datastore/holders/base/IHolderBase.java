package com.msgkatz.ratesapp.data.repo.datastore.holders.base;

import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import io.reactivex.Flowable;

/**
 * Created by msgkatz on 15/08/2018.
 */

public interface IHolderBase<T> {
    Flowable<Optional<T>> getData();
    void flush();
}
