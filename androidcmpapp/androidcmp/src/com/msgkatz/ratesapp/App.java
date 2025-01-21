package com.msgkatz.ratesapp;

import android.app.Activity;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.msgkatz.ratesapp.di.app.AppComponent;
import com.msgkatz.ratesapp.di.app.DaggerAppComponent;
//import com.msgkatz.ratesapp.presentation.ui.chart2.base.di.ChartDepsProvider;
//import com.msgkatz.ratesapp.presentation.ui.chart2.base.di.ChartDepsStore;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
//import dagger.android.HasActivityInjector;
import dagger.android.HasAndroidInjector;

/**
 * Created by msgkatz on 20/07/2018.
 */

public class App extends MultiDexApplication /** implements HasAndroidInjector HasActivityInjector**/ {

    private static App sInstance;
    private static Context appContext;
    //private static AppComponentOld component;

    public AppComponent appComponent;

//    @Inject
//    DispatchingAndroidInjector<Object> dispatchingAndroidInjector;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();


        appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build();
                //.inject(this);

        appContext = getApplicationContext();
        sInstance = this;

    }

    public static Context getAppContext() {
        return appContext;
    }

    public static App getInstance() {
        return sInstance;
    }

//    @Override
//    //public AndroidInjector<Object> androidInjector() {
//        return dispatchingAndroidInjector;
//    }

//    @Override
//    public AndroidInjector<Object> androidInjector() {
//        return dispatchingAndroidInjector;
//    }

    //@Override
//    public AndroidInjector<Activity> androidInjector() {
//        return dispatchingAndroidInjector;
//    }
}
