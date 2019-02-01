package com.msgkatz.ratesapp;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.msgkatz.ratesapp.di.app.DaggerAppComponent;
import javax.inject.Inject;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by msgkatz on 20/07/2018.
 */

public class App extends MultiDexApplication implements HasActivityInjector {

    private static App sInstance;
    private static Context appContext;
    //private static AppComponentOld component;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);

        appContext = getApplicationContext();
        sInstance = this;

    }

    public static Context getAppContext() {
        return appContext;
    }

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
