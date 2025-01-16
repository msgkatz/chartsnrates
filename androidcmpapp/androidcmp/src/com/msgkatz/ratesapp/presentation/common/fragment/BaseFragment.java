package com.msgkatz.ratesapp.presentation.common.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msgkatz.ratesapp.di.common.BaseFragmentModule;
import com.msgkatz.ratesapp.presentation.common.Layout;
import com.msgkatz.ratesapp.presentation.common.mvp.BasePresenter;
import com.msgkatz.ratesapp.presentation.common.mvp.BaseView;
import com.msgkatz.ratesapp.utils.Logs;

import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
//import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by msgkatz on 09/09/2018.
 */

public abstract class BaseFragment extends BaseLayoutDummyFragment implements BaseView /**, HasSupportFragmentInjector**/ {

    @Inject
    protected Context activityContext;

    // Note that this should not be used within a child fragment.
    @Inject
    @Named(BaseFragmentModule.CHILD_FRAGMENT_MANAGER)
    protected FragmentManager childFragmentManager;

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Perform injection here before M, L (API 22) and below because onAttach(Context)
            // is not yet available at L.
            AndroidSupportInjection.inject(this);
        }
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Perform injection here for M (API 23) due to deprecation of onAttach(Activity).
            AndroidSupportInjection.inject(this);
        }
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
