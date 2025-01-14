package com.msgkatz.ratesapp.presentation.ui.main.base;

import com.msgkatz.ratesapp.di.common.BaseFragmentModule;


import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

@Module(includes = BaseFragmentModule.class)
abstract class MainFragmentNewModule {

//    @Provides
//    @PerFragment
//    @Nullable
//    static String provideQuoteAssetName(QuoteAssetFragmentNew mainFragment) {
//        if (mainFragment.getArguments() != null)
//            return mainFragment.getArguments().getString(QuoteAssetFragmentNew.KEY_QUOTE_ASSET_NAME);
//        else
//            return null;
//    }
//
//    @Binds
//    @Named(BaseFragmentModule.FRAGMENT)
//    @PerFragment
//    abstract Fragment fragment(QuoteAssetFragmentNew mainFragment);

//    @PerFragment
//    @Named(BaseFragmentModule.FRAGMENT)
//    @Binds
//    abstract QuoteAssetViewModel quoteAssetViewModel(QuoteAssetViewModel viewModel);
}
