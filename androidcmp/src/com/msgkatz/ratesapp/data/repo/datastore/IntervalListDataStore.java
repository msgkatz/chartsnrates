package com.msgkatz.ratesapp.data.repo.datastore;

import android.content.Context;

import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.data.repo.datastore.holders.IntervalListHolder;
import com.msgkatz.ratesapp.di.app.AppModule;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class IntervalListDataStore {

    private final IntervalListHolder intervalListHolder;

    @Inject
    public IntervalListDataStore(@Named(AppModule.APP_CONTEXT) Context appContext, InnerModel innerModel)
    {
        this.intervalListHolder = new IntervalListHolder(appContext, innerModel);
    }

    public Observable<Optional<List<Interval>>> getIntervalData()
    {
        return this.intervalListHolder.getData().toObservable();
    }

    public Observable<Optional<Interval>> getIntervalByName(String interval)
    {
        return this.intervalListHolder.getDataByName(interval).toObservable();
    }

}
