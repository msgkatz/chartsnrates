package com.msgkatz.ratesapp.old.data.repo.datastore.holders.base;

/**
 * Created by msgkatz on 30/08/2018.
 */

@Deprecated
public interface ICrossHolderBase<T> {

    T getInternal();
    void setInternal(T data);
}
