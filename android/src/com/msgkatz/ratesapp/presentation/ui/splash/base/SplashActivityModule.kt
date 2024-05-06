package com.msgkatz.ratesapp.presentation.ui.splash.base

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.msgkatz.ratesapp.di.common.BaseActivityModule
import com.msgkatz.ratesapp.di.scope.PerActivity
import com.msgkatz.ratesapp.presentation.ui.main.QuoteAssetPresenter
import com.msgkatz.ratesapp.presentation.ui.main.base.BaseMainPresenter
import com.msgkatz.ratesapp.presentation.ui.splash.SplashActivityNew
import com.msgkatz.ratesapp.presentation.ui.splash.SplashViewModel
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class SplashActivityModule {



    //NOTE:  IF you want to have something be only in the Fragment scope but not activity mark a
    //@provides or @Binds method as @FragmentScoped.  Use case is when there are multiple fragments
    //in an activity but you do not want them to share all the same objects.
    @Binds
    @PerActivity
    abstract fun activity(mainActivity: SplashActivityNew): Activity

    @Binds
    abstract fun splashViewModel(model: SplashViewModel): ViewModel


}