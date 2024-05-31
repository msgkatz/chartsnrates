package com.msgkatz.ratesapp.presentation.ui.main.base;

import android.app.Activity;

import com.msgkatz.ratesapp.di.common.BaseActivityModule;


import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = BaseActivityModule.class)
public abstract class MainActivityModule {



//    @PerFragment
//    @ContributesAndroidInjector(modules = MainFragmentNewModule.class)
//    abstract QuoteAssetFragmentNew mainFragmentNewInjector();
//
//    //NOTE:  IF you want to have something be only in the Fragment scope but not activity mark a
//    //@provides or @Binds method as @FragmentScoped.  Use case is when there are multiple fragments
//    //in an activity but you do not want them to share all the same objects.
//
//
//    @Binds
//    @PerActivity
//    abstract Activity activity(MainActivity mainActivity);
}
