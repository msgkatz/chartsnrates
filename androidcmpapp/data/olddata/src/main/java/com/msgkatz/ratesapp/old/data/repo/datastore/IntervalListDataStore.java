package com.msgkatz.ratesapp.old.data.repo.datastore;

import android.content.Context;

import com.msgkatz.ratesapp.old.data.DataParams;
import com.msgkatz.ratesapp.old.data.repo.InnerModel;
import com.msgkatz.ratesapp.old.data.repo.datastore.holders.IntervalListHolder;
import com.msgkatz.ratesapp.old.domain.entities.IntervalJava;
import com.msgkatz.ratesapp.old.domain.interactors.base.Optional;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class IntervalListDataStore {

    private final IntervalListHolder intervalListHolder;

    @Inject
    public IntervalListDataStore(@Named(DataParams.APP_CONTEXT) Context appContext, InnerModel innerModel)
    {
        this.intervalListHolder = new IntervalListHolder(appContext, innerModel);
    }

    public Observable<Optional<List<IntervalJava>>> getIntervalData()
    {
        return this.intervalListHolder.getData().toObservable();
    }

    public Observable<Optional<IntervalJava>> getIntervalByName(String interval)
    {
        return this.intervalListHolder.getDataByName(interval).toObservable();
    }

}
