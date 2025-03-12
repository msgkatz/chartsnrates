package com.msgkatz.ratesapp.feature.common.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.msgkatz.ratesapp.feature.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.feature.common.mvp.BaseView;
import com.msgkatz.ratesapp.feature.common.utils.Logs;


import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
//import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by msgkatz on 09/09/2018.
 */

public abstract class BaseFragment extends BaseLayoutDummyFragment implements BaseView /**, HasSupportFragmentInjector**/ {

    @Inject
    protected Context activityContext;



    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getPresenter() != null)
            getPresenter().setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Logs.d("onActivityCreated %s", this.getClass().getSimpleName());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Logs.d("onStart %s", this.getClass().getSimpleName());
        if (getPresenter() != null) {
            getPresenter().setView(this);
            getPresenter().onStart();
        }

        super.onStart();
    }

    @Override
    public void onStop() {
        Logs.d("onStop %s", this.getClass().getSimpleName());
        if (getPresenter() != null)
            getPresenter().onStop();

        super.onStop();
    }

    @Override
    public void onDestroyView() {

        if (getPresenter() != null)
            getPresenter().setView(null);
        super.onDestroyView();
    }

//    @Override
//    public AndroidInjector<Fragment> supportFragmentInjector() {
//        return childFragmentInjector;
//    }

    public String getFragmentName()
    {
        return this.getClass().getSimpleName();
    }

    public abstract BasePresenter getPresenter();

    public void setConfigurationChange(boolean isLandscape) {

    }
}
