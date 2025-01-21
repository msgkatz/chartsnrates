package com.msgkatz.ratesapp.feature.chartgdx.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.msgkatz.ratesapp.feature.chartgdx.base.di.PerFragment
//import com.msgkatz.ratesapp.di.common.BaseFragmentModule
//import com.msgkatz.ratesapp.di.scope.PerFragment
import com.msgkatz.ratesapp.feature.chartgdx.widget.ChartGdxFragmentNew
import com.msgkatz.ratesapp.feature.chartgdx.widget.ChartGdxViewModel
import dagger.Binds
import dagger.Module

//@Module()
@Module(includes = [
    //BaseFragmentModule::class
])
abstract class ChildFragmentNewModule {

    @Binds
    //@Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @PerFragment
    abstract fun chartGdxFragmentNew(chartGdxFragmentNew: ChartGdxFragmentNew): Fragment

    @Binds
    @PerFragment
    abstract fun chartGdxViewModel(model: ChartGdxViewModel): ViewModel

//    @Binds
//    //@Named(BaseChildFragmentModule.CHILD_FRAGMENT)
//    @PerFragment
//    abstract fun chartGdxFragment(chartGdxFragment: ChartGdxFragment): Fragment
//
//    @PerFragment
//    @Binds
//    abstract fun chartGdxPresenter(presenter: ChartGdxPresenter): BaseChartGdxPresenter
}