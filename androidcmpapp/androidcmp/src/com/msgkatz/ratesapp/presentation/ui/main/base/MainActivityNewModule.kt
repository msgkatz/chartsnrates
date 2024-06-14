package com.msgkatz.ratesapp.presentation.ui.main.base;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.msgkatz.ratesapp.di.common.BaseActivityModule;
import com.msgkatz.ratesapp.di.scope.PerActivity;
import com.msgkatz.ratesapp.presentation.ui.main.widget.MainActivityNew;
import com.msgkatz.ratesapp.presentation.ui.main.widget.MainActivityNewViewModel;

import dagger.Binds;
import dagger.Module;

@Module(includes = [BaseActivityModule::class])
abstract class MainActivityNewModule {

    @Binds
    @PerActivity
    abstract fun activity(mainActivityNew: MainActivityNew): Activity

    @Binds
    abstract fun mainActivityNewViewModel(model: MainActivityNewViewModel): ViewModel
}