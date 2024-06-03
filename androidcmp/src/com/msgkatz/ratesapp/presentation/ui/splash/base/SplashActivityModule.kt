package com.msgkatz.ratesapp.presentation.ui.splash.base


import com.msgkatz.ratesapp.di.common.BaseActivityModule

import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class SplashActivityModule {



    //NOTE:  IF you want to have something be only in the Fragment scope but not activity mark a
    //@provides or @Binds method as @FragmentScoped.  Use case is when there are multiple fragments
    //in an activity but you do not want them to share all the same objects.
//    @Binds
//    @PerActivity
//    abstract fun activity(mainActivity: SplashActivityNew): Activity

//    @Binds
//    abstract fun splashViewModel(model: SplashViewModel): ViewModel


}