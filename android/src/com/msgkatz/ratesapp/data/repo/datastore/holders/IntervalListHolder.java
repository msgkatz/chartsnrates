package com.msgkatz.ratesapp.data.repo.datastore.holders;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msgkatz.ratesapp.data.entities.IntervalDT;
import com.msgkatz.ratesapp.data.entities.mappers.IntervalDTDataMapper;
import com.msgkatz.ratesapp.data.repo.InnerModel;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.utils.Logs;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class IntervalListHolder {

    private final static String TAG = IntervalListHolder.class.getSimpleName();

    private final Context appContext;
    private final InnerModel innerModel;
    //rivate List<Interval> intervalList = new ArrayList<>();
    private final Map<String, Interval> intervalMap;

    public IntervalListHolder(Context appContext, InnerModel innerModel)
    {
        this.appContext = appContext;
        this.innerModel = innerModel;
        this.intervalMap = innerModel.getIntervalMap();
    }

    public Flowable<Optional<List<Interval>>> getData()
    {
        if (intervalMap.size() == 0)
        {

            return Flowable.create(new FlowableOnSubscribe<Optional<List<Interval>>>() {
                @Override
                public void subscribe(FlowableEmitter<Optional<List<Interval>>> emitter) throws Exception {

                    List<Interval> intervals = IntervalDTDataMapper.transform(loadIntervalsFromAppAssets());

                    if (intervals != null && intervals.size() > 0)
                    {

                        for (Interval interval : intervals)
                            intervalMap.put(interval.getValue(), interval);

                        emitter.onNext(new Optional<List<Interval>>(loadIntervalsSorted()));
                    }
                    emitter.onComplete();
                }
            }, BackpressureStrategy.DROP);

        }
        else
        {
            return Flowable.just(new Optional<List<Interval>>(loadIntervalsSorted()));
        }

    }

    public Flowable<Optional<Interval>> getDataByName(String interval)
    {
        if (intervalMap.size() == 0)
        {

            return Flowable.create(new FlowableOnSubscribe<Optional<Interval>>() {
                @Override
                public void subscribe(FlowableEmitter<Optional<Interval>> emitter) throws Exception {
                    List<Interval> intervals = IntervalDTDataMapper.transform(loadIntervalsFromAppAssets());
                    if (intervals != null && intervals.size() > 0)
                    {

                        for (Interval interval : intervals)
                            intervalMap.put(interval.getValue(), interval);

                        emitter.onNext(new Optional<Interval>(intervalMap.get(interval)));
                    }
                    emitter.onComplete();
                }
            }, BackpressureStrategy.DROP);

        }
        else
        {
            return Flowable.just(new Optional<Interval>(intervalMap.get(interval)));
        }

    }

    private List<Interval> loadIntervalsSorted() {
        List<Interval> intervals = new ArrayList<>(intervalMap.values());

        Collections.sort(intervals, new Comparator<Interval>() {
            @Override
            public int compare(Interval o1, Interval o2) {
                return Integer.valueOf(o1.getId()).compareTo(Integer.valueOf(o2.getId()));
            }
        });

        return intervals;
    }

    private List<IntervalDT> loadIntervalsFromAppAssets()
    {
        Logs.e(TAG, "loading intervals from json");
        List<IntervalDT> intervals = null;

        try {
            String json = null;
            InputStream is = this.appContext.getAssets().open("interval_names.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            Type collectionType = new TypeToken<List<IntervalDT>>(){}.getType();
            intervals = new Gson().fromJson(json, collectionType);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        Logs.e(TAG, "loaded intervals from json=" + intervals + ", cnt = " + (intervals == null ? "null" : intervals.size()));
        return intervals;

    }
}
