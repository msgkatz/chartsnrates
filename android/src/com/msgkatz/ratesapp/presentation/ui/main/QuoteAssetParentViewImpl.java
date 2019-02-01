package com.msgkatz.ratesapp.presentation.ui.main;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import com.msgkatz.ratesapp.presentation.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.presentation.ui.main.base.BaseMainDummyFragment;

import javax.inject.Inject;

import static android.arch.lifecycle.Lifecycle.State.STARTED;

/**
 * Created by msgkatz on 11/09/2018.
 */

public class QuoteAssetParentViewImpl extends BaseMainDummyFragment implements QuoteAssetParentView {

    private Context context;
    private Lifecycle lifecycle;
    private boolean enabled = false;

    @Inject
    QuoteAssetPresenter mQuoteAssetPresenter;

    public QuoteAssetParentViewImpl(Context context, Lifecycle lifecycle)
    {
        super(context, lifecycle);
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    void start() {
//        if (enabled) {
//            // connect
//        }
//    }
//
//    public void enable() {
//        enabled = true;
//        if (lifecycle.getCurrentState().isAtLeast(STARTED)) {
//            // connect if not connected
//        }
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    void stop() {
//        // disconnect if connected
//    }

//    @Override
//    public void inject() {
//        getComponent().inject(this);
//    }

    @Override
    public BasePresenter getPresenter() {
        return mQuoteAssetPresenter;
    }
}
