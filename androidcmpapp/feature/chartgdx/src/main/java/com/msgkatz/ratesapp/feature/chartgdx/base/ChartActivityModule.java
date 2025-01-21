package com.msgkatz.ratesapp.feature.chartgdx.base;



import dagger.Module;

@Module(
        //includes = BaseActivityModule.class
)
public abstract class ChartActivityModule {

//    @PerFragment
//    @ContributesAndroidInjector(modules = ParentFragmentModule.class)
//    abstract ChartParentFragment parentFragmentInjector();


    //NOTE:  IF you want to have something be only in the Fragment scope but not activity mark a
    //@provides or @Binds method as @FragmentScoped.  Use case is when there are multiple fragments
    //in an activity but you do not want them to share all the same objects.

//    @Binds
//    @PerActivity
//    abstract Activity activity(ChartActivity сhartActivity);
}
