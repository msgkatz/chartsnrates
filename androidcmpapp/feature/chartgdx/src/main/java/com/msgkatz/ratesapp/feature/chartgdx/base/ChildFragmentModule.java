package com.msgkatz.ratesapp.feature.chartgdx.base;



import dagger.Module;

@Module(includes = {
        //BaseChildFragmentModule.class,
})
public abstract class ChildFragmentModule {
    /**
     * As per the contract specified in {@link BaseChildFragmentModule}; "This must be included in
     * all child fragment modules, which must provide a concrete implementation of the child
     * {@link BaseChartGdxFragment} and named {@link BaseChildFragmentModule#CHILD_FRAGMENT}..
     *
     * @param chartGdxFragment the example 3 child fragment
     * @return the fragment
     */
//    @Binds
//    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
//    @PerChildFragment
//    abstract Fragment chartGdxFragment(ChartGdxFragment chartGdxFragment);
//
//    //@PerActivity
//    //@PerFragment
//    @PerChildFragment
//    @Binds
//    abstract BaseChartGdxPresenter chartGdxPresenter(ChartGdxPresenter presenter);

}
