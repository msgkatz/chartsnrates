package com.msgkatz.ratesapp.feature.chartgdx.base;



import dagger.Module;

@Module(
        //includes = BaseFragmentModule.class
)
abstract class ParentFragmentModule {

    /**
     * Provides the injector for the {@link BaseChartGdxFragment}, which has access to the
     * dependencies provided by this fragment and activity and application instance
     * (singleton scoped objects).
     */
//    @PerChildFragment
//    @ContributesAndroidInjector(modules = ChildFragmentModule.class)
//    abstract ChartGdxFragment childFragmentInjector();
//
////    //@PerActivity
////    //@PerFragment
////    @PerChildFragment
////    @Binds
////    abstract BaseChartGdxPresenter chartGdxPresenter(ChartGdxPresenter presenter);
//
//    /**
//     * As per the contract specified in {@link BaseFragmentModule}; "This must be included in all
//     * fragment modules, which must provide a concrete implementation of {@link BaseChartFragment}
//     * and named {@link BaseFragmentModule#FRAGMENT}.
//     *
//     * @param parentFragment parent fragment
//     * @return the fragment
//     */
//    @Binds
//    @Named(BaseFragmentModule.FRAGMENT)
//    @PerFragment
//    abstract Fragment chartFragment(ChartParentFragment parentFragment);
//
//
//    //@PerActivity
//    @PerFragment
//    @Binds
//    abstract BaseChartParentPresenter chartParentPresenter(ChartParentPresenter presenter);
}
