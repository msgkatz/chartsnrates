package com.msgkatz.ratesapp.feature.chartgdx.base

import android.app.Activity
import androidx.lifecycle.ViewModel

import com.msgkatz.ratesapp.feature.chartgdx.base.di.PerActivity

import com.msgkatz.ratesapp.feature.chartgdx.widget.ChartActivityNew
import com.msgkatz.ratesapp.feature.chartgdx.widget.ChartGdxFragmentNew
import com.msgkatz.ratesapp.feature.chartgdx.widget.ChartParentViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [
    //BaseActivityModule::class
])
abstract class ChartActivityNewModule {

//    @PerFragment
//    @ContributesAndroidInjector(modules = [ChildFragmentNewModule::class])
//    abstract fun childFragmentInjector(): ChartGdxFragmentNew

    @Binds
    @PerActivity
    abstract fun activity(chartActivityNew: ChartActivityNew): Activity

    @Binds
    abstract fun chartActivityNewViewModel(model: ChartParentViewModel): ViewModel
}