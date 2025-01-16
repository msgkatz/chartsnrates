package com.msgkatz.ratesapp.presentation.ui.chart2.base

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.msgkatz.ratesapp.di.scope.PerActivity
import com.msgkatz.ratesapp.di.scope.PerFragment
import com.msgkatz.ratesapp.presentation.ui.chart2.widget.ChartActivityNew
import com.msgkatz.ratesapp.presentation.ui.chart2.widget.ChartGdxFragmentNew
import com.msgkatz.ratesapp.presentation.ui.chart2.widget.ChartParentViewModel
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