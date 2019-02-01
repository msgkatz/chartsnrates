package com.msgkatz.ratesapp.presentation.ui.main.base;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.msgkatz.ratesapp.di.common.BaseFragmentModule;
import com.msgkatz.ratesapp.di.scope.PerFragment;
import com.msgkatz.ratesapp.presentation.ui.main.QuoteAssetFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = BaseFragmentModule.class)
abstract class MainFragmentModule {

    @Provides
    @PerFragment
    @Nullable
    static String provideQuoteAssetName(QuoteAssetFragment mainFragment) {
        if (mainFragment.getArguments() != null)
            return mainFragment.getArguments().getString(QuoteAssetFragment.KEY_QUOTE_ASSET_NAME);
        else
            return null;
    }

    @Binds
    @Named(BaseFragmentModule.FRAGMENT)
    @PerFragment
    abstract Fragment fragment(QuoteAssetFragment mainFragment);



}
