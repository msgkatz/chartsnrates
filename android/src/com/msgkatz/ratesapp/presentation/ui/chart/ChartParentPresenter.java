package com.msgkatz.ratesapp.presentation.ui.chart;

import android.os.Handler;

import com.msgkatz.ratesapp.App;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.domain.entities.Tool;
import com.msgkatz.ratesapp.domain.interactors.GetIntervals;
import com.msgkatz.ratesapp.domain.interactors.GetTools;
import com.msgkatz.ratesapp.domain.interactors.base.Optional;
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver;
import com.msgkatz.ratesapp.presentation.common.messaging.IRxBus;
import com.msgkatz.ratesapp.presentation.entities.events.BaseEvent;
import com.msgkatz.ratesapp.presentation.entities.events.NewIntervalEvent;
import com.msgkatz.ratesapp.presentation.entities.events.PriceEvent;
import com.msgkatz.ratesapp.presentation.ui.chart.base.BaseChartParentPresenter;
import com.msgkatz.ratesapp.presentation.ui.chart.base.BaseChartPresenter;
import com.msgkatz.ratesapp.presentation.ui.main.base.BaseMainPresenter;
import com.msgkatz.ratesapp.utils.Logs;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by msgkatz on 14/09/2018.
 */

public class ChartParentPresenter extends BaseChartParentPresenter {

    private final static String TAG = ChartParentPresenter.class.getSimpleName();

    @Inject
    GetTools mGetTools;

    @Inject
    GetIntervals mGetIntervals;

    @Inject
    IRxBus rxBus;


    private String mToolName;
    private Tool mTool;
    private List<Interval> mIntervals;

    private ResponseObserver observerTools;
    private ResponseObserver observerIntervals;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ChartParentPresenter()
    {}

    @Override
    public void onStart()
    {
        observerTools = new ResponseObserver<Optional<Map<String, Tool>>, Map<String, Tool>>() {

            @Override
            public void doNext(Map<String, Tool> stringToolMap) {
                if (stringToolMap != null)
                {
                    mTool = stringToolMap.get(mToolName);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (getView() != null)
                                getView().updateTitle(mTool);
                        }
                    });
                }
            }
        };

        observerIntervals = new ResponseObserver<Optional<List<Interval>>, List<Interval>>() {
            @Override
            public void doNext(List<Interval> intervalList) {
                Logs.d(TAG, "getting intervals: " + intervalList);
                if (intervalList != null) {
                    mIntervals = intervalList;
                    if (getView() != null)
                        getView().updateIntervals(mIntervals);
                }
            }

            @Override
            public void onError(Throwable exception) {

                super.onError(exception);
            }
        };

        if (mTool == null)
        {
            mGetTools.execute(observerTools, null);
        }

        if (mIntervals == null)
        {
            mGetIntervals.execute(observerIntervals, null);
        }

        initEvents();

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

        observerTools.dispose();
        observerIntervals.dispose();
        disposables.clear();

    }

    public void setPrice(double newPrice)
    {
        if (getView() != null)
            getView().updatePrice(newPrice);
    }

    public void provideToolName(String toolName) {
        this.mToolName = toolName;
    }

    public void provideNewInterval(Interval interval) {
        if (rxBus != null)
            rxBus.send(new NewIntervalEvent(interval));
    }

    private void initEvents()
    {
        disposables.add(rxBus
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {

                        if (object instanceof BaseEvent) {
                            if (object instanceof PriceEvent)
                            {
                                PriceEvent priceEvent = (PriceEvent) object;
                                setPrice(priceEvent.getPrice());
                            }
                        }

                    }
                }));
    }

}
