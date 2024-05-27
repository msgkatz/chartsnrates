package com.msgkatz.ratesapp.presentation.ui.chart.base

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.msgkatz.ratesapp.di.common.BaseActivityModule
import com.msgkatz.ratesapp.di.scope.PerActivity
import com.msgkatz.ratesapp.presentation.ui.chart.widget.ChartActivityNew
import com.msgkatz.ratesapp.presentation.ui.chart.widget.ChartParentViewModel
import com.msgkatz.ratesapp.presentation.ui.main.widget.MainActivityNew
import com.msgkatz.ratesapp.presentation.ui.main.widget.MainActivityNewViewModel
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class ChartActivityNewModule {

    @Binds
    @PerActivity
    abstract fun activity(chartActivityNew: ChartActivityNew): Activity

    @Binds
    abstract fun ChartActivityNewViewModel(model: ChartParentViewModel): ViewModel
}