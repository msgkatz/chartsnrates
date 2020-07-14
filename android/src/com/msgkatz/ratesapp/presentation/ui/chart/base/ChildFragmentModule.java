package com.msgkatz.ratesapp.presentation.ui.chart.base;

import com.msgkatz.ratesapp.di.common.BaseChildFragmentModule;
import com.msgkatz.ratesapp.di.scope.PerChildFragment;
import com.msgkatz.ratesapp.presentation.ui.chart.ChartGdxFragment;
import com.msgkatz.ratesapp.presentation.ui.chart.ChartGdxPresenter;

import javax.inject.Named;

import androidx.fragment.app.Fragment;
import dagger.Binds;
import dagger.Module;

@Module(includes = {BaseChildFragmentModule.class,})
public abstract class ChildFragmentModule {
    /**
     * As per the contract specified in {@link BaseChildFragmentModule}; "This must be included in
     * all child fragment modules, which must provide a concrete implementation of the child
     * {@link BaseChartGdxFragment} and named {@link BaseChildFragmentModule#CHILD_FRAGMENT}..
     *
     * @param chartGdxFragment the example 3 child fragment
     * @return the fragment
     */
    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @PerChildFragment
    abstract Fragment chartGdxFragment(ChartGdxFragment chartGdxFragment);

    //@PerActivity
    //@PerFragment
    @PerChildFragment
    @Binds
    abstract BaseChartGdxPresenter chartGdxPresenter(ChartGdxPresenter presenter);

}
