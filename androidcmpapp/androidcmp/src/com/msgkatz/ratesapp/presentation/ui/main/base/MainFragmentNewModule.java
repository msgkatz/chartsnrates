package com.msgkatz.ratesapp.presentation.ui.main.base;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.msgkatz.ratesapp.di.common.BaseFragmentModule;
import com.msgkatz.ratesapp.di.scope.PerFragment;
import com.msgkatz.ratesapp.presentation.ui.main.widget.QuoteAssetViewModel;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

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

    @PerFragment
    @Named(BaseFragmentModule.FRAGMENT)
    @Binds
    abstract QuoteAssetViewModel quoteAssetViewModel(QuoteAssetViewModel viewModel);
}
