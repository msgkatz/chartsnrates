package com.msgkatz.ratesapp.di.app;

import com.msgkatz.ratesapp.di.scope.PerActivity;
import com.msgkatz.ratesapp.presentation.ui.chart2.base.ChartActivityNewModule;
import com.msgkatz.ratesapp.presentation.ui.chart2.widget.ChartActivityNew;
import com.msgkatz.ratesapp.presentation.ui.main.base.MainActivityNewModule;
import com.msgkatz.ratesapp.presentation.ui.main.widget.MainActivityNew;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Deprecated
@Module
public abstract class ActivityBindingModule {

//    @PerActivity
//    @ContributesAndroidInjector(modules = SplashActivityModule.class)
//    abstract SplashActivityNew bindSplashActivityNew();
//
//    @PerActivity
//    @ContributesAndroidInjector
//    abstract SplashActivity bindSplashActivity();
//
//    @PerActivity
//    @ContributesAndroidInjector(modules = MainActivityModule.class)
//    abstract MainActivity bindMainActivity();


    @PerActivity
    @ContributesAndroidInjector(modules = MainActivityNewModule.class)
    abstract MainActivityNew bindMainActivityNew();


//    @PerActivity
//    @ContributesAndroidInjector(modules = ChartActivityModule.class)
//    abstract ChartActivity bindChartActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ChartActivityNewModule.class)
    abstract ChartActivityNew bindChartActivityNew();
}
