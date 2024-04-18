package com.msgkatz.ratesapp.di.app;

import com.msgkatz.ratesapp.di.scope.PerActivity;
import com.msgkatz.ratesapp.presentation.ui.chart.ChartActivity;
import com.msgkatz.ratesapp.presentation.ui.chart.base.ChartActivityModule;
import com.msgkatz.ratesapp.presentation.ui.main.MainActivity;
import com.msgkatz.ratesapp.presentation.ui.main.base.MainActivityModule;
import com.msgkatz.ratesapp.presentation.ui.splash.SplashActivityOld;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @PerActivity
    @ContributesAndroidInjector
    abstract SplashActivityOld bindSplashActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ChartActivityModule.class)
    abstract ChartActivity bindChartActivity();
}
