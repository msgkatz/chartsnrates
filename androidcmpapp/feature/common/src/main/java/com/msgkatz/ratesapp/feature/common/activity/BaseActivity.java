package com.msgkatz.ratesapp.feature.common.activity;


import android.os.Bundle;
import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
//import dagger.android.HasAndroidInjector;
//import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by msgkatz on 15/08/2018.
 */

public class BaseActivity extends BaseLayoutDummyActivity /**implements HasAndroidInjector HasSupportFragmentInjector**/ {

//    @Inject
//    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public AndroidInjector<Fragment> supportFragmentInjector() {
//        return fragmentDispatchingAndroidInjector;
//    }

    @Override
    public void setFullscreenMode(boolean isFullscreen) {

    }

//    @Override
//    public void addMenuProvider(@NonNull MenuProvider provider, @NonNull LifecycleOwner owner, @NonNull Lifecycle.State state) {
//
//    }

//    @Override
//    public AndroidInjector<Object> androidInjector() {
//        return (AndroidInjector<Object>)fragmentDispatchingAndroidInjector;
//    }
}
