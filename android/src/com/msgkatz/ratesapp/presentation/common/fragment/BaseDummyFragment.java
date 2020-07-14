package com.msgkatz.ratesapp.presentation.common.fragment;

import android.content.Context;
import com.msgkatz.ratesapp.presentation.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.presentation.common.mvp.BaseView;
import com.msgkatz.ratesapp.presentation.ui.main.QuoteAssetParentView;

import java.lang.ref.WeakReference;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Created by msgkatz on 11/09/2018.
 */

public abstract class BaseDummyFragment implements LifecycleObserver, BaseView {

    private WeakReference<Context> context;
    private WeakReference<Lifecycle> lifecycle;

    public BaseDummyFragment(Context context, Lifecycle lifecycle)
    {
        this.context = new WeakReference<Context>(context);
        this.lifecycle = new WeakReference<Lifecycle>(lifecycle);

    }

    public void create()
    {
        if (getPresenter() != null) {
            getPresenter().setView(this);
        }

        //onStart();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start()
    {
        create();

        getPresenter().setView(this);
        getPresenter().onStart();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop()
    {
        //onStop();
        getPresenter().onStop();
    }

    public void destroy()
    {
        if (getPresenter() != null)
            getPresenter().setView(null);
    }

    public abstract BasePresenter getPresenter();

}
