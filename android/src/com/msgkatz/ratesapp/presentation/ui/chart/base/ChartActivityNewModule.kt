package com.msgkatz.ratesapp.presentation.ui.chart.base

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.msgkatz.ratesapp.di.common.BaseActivityModule
import com.msgkatz.ratesapp.di.scope.PerActivity
import com.msgkatz.ratesapp.di.scope.PerFragment
import com.msgkatz.ratesapp.presentation.ui.chart.ChartGdxFragment
import com.msgkatz.ratesapp.presentation.ui.chart.widget.ChartActivityNew
import com.msgkatz.ratesapp.presentation.ui.chart.widget.ChartGdxFragmentNew
import com.msgkatz.ratesapp.presentation.ui.chart.widget.ChartParentViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [BaseActivityModule::class])
abstract class ChartActivityNewModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [ChildFragmentNewModule::class])
    abstract fun childFragmentInjector(): ChartGdxFragmentNew

    @Binds
    @PerActivity
    abstract fun activity(chartActivityNew: ChartActivityNew): Activity

    @Binds
    abstract fun chartActivityNewViewModel(model: ChartParentViewModel): ViewModel
}