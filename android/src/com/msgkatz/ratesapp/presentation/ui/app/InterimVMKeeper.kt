package com.msgkatz.ratesapp.presentation.ui.app

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.msgkatz.ratesapp.domain.interactors.GetAssets
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo
import com.msgkatz.ratesapp.domain.interactors.GetQuoteAssetsMap
import com.msgkatz.ratesapp.domain.interactors.GetToolListPrices
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer
import com.msgkatz.ratesapp.presentation.ui.main.widget.QuoteAssetViewModel
import com.msgkatz.ratesapp.presentation.ui.splash.SplashViewModel

class InterimVMKeeper(
    private val owner: ViewModelStoreOwner,
    private val mGetAssets: GetAssets,
    private val mGetPlatformInfo: GetPlatformInfo,
    private val mGetQuoteAssetsMap: GetQuoteAssetsMap,
    private val mGetToolListPrices: GetToolListPrices,
    private val tabInfoStorer: TabInfoStorer
) {
    fun makeSplash(): SplashViewModel {
        val viewModelFactory = SplashViewModelFactory(mGetAssets, mGetPlatformInfo)
        val viewModel = ViewModelProvider(owner, viewModelFactory)[SplashViewModel::class.java]
        return viewModel
    }

    fun makeQuoteAsset(quoteAssetName: String?): QuoteAssetViewModel {
        val viewModelFactory = QuoteAssetViewModelFactory(quoteAssetName, mGetQuoteAssetsMap, mGetToolListPrices, tabInfoStorer)
        val viewModel = ViewModelProvider(owner, viewModelFactory)[QuoteAssetViewModel::class.java]
        return viewModel
    }

    fun makeQuoteAsset33(quoteAssetName: String?, _owner: ViewModelStoreOwner): QuoteAssetViewModel {
        val viewModelFactory = QuoteAssetViewModelFactory(quoteAssetName, mGetQuoteAssetsMap, mGetToolListPrices, tabInfoStorer)
        val viewModel = ViewModelProvider(_owner, viewModelFactory)[QuoteAssetViewModel::class.java]
        return viewModel
    }


    fun makeQuoteAsset2(quoteAssetName: String?): QuoteAssetViewModel {
        val viewModelFactory = QuoteAssetViewModelFactory(quoteAssetName, mGetQuoteAssetsMap, mGetToolListPrices, tabInfoStorer)
        val viewModel = ViewModelProvider(owner.viewModelStore, viewModelFactory, MutableCreationExtras().apply {
            set(MY_ARGS_KEY, quoteAssetName!!)
        })[quoteAssetName!!, QuoteAssetViewModel::class.java]
        //[QuoteAssetViewModel::class.java]
        return viewModel
    }
    fun makeQuoteAsset3(quoteAssetName: String?, _owner: ViewModelStoreOwner): QuoteAssetViewModel {
        val viewModelFactory = QuoteAssetViewModelFactory(quoteAssetName, mGetQuoteAssetsMap, mGetToolListPrices, tabInfoStorer)
        val viewModel = ViewModelProvider(_owner.viewModelStore, viewModelFactory, MutableCreationExtras().apply {
            set(MY_ARGS_KEY, quoteAssetName!!)
        })[quoteAssetName!!, QuoteAssetViewModel::class.java]
        //[QuoteAssetViewModel::class.java]
        return viewModel
    }


    //TODO needs naming fixes
    fun makeQuoteAsset4(quoteAssetName: String?, _owner: ViewModelStoreOwner): QuoteAssetViewModel {
        val viewModelFactory = QuoteAssetSavedStateViewModelFactory(quoteAssetName, mGetQuoteAssetsMap, mGetToolListPrices, tabInfoStorer)
        val viewModel = ViewModelProvider(_owner, viewModelFactory)[QuoteAssetViewModel::class.java]
        return viewModel
    }


}
class SplashViewModelFactory(
    private val mGetAssets: GetAssets,
    private val mGetPlatformInfo: GetPlatformInfo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(mGetAssets, mGetPlatformInfo ) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}

@JvmField
val MY_ARGS_KEY = object : CreationExtras.Key<String> {}

class QuoteAssetViewModelFactory(private val quoteAssetName: String?,
                                   private val mGetQuoteAssetsMap: GetQuoteAssetsMap,
                                   private val mGetToolListPrices: GetToolListPrices,
                                   private val tabInfoStorer: TabInfoStorer
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

//        throw UnsupportedOperationException(
//            "Factory.create(String) is unsupported.  This Factory requires " +
//                    "`CreationExtras` to be passed into `create` method."
//        )

        if (modelClass.isAssignableFrom(QuoteAssetViewModel::class.java)){
            return QuoteAssetViewModel(quoteAssetName,
                mGetQuoteAssetsMap,
                mGetToolListPrices,
                tabInfoStorer) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(QuoteAssetViewModel::class.java)){
            //val name = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            val name = checkNotNull(extras[MY_ARGS_KEY])

            return QuoteAssetViewModel(name, //quoteAssetName,
                mGetQuoteAssetsMap,
                mGetToolListPrices,
                tabInfoStorer) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}

class QuoteAssetSavedStateViewModelFactory(private val quoteAssetName: String?,
                                 private val mGetQuoteAssetsMap: GetQuoteAssetsMap,
                                 private val mGetToolListPrices: GetToolListPrices,
                                 private val tabInfoStorer: TabInfoStorer
) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(QuoteAssetViewModel::class.java)){
            handle.set("quoteAssetName", quoteAssetName)
            return QuoteAssetViewModel(quoteAssetName,
                mGetQuoteAssetsMap,
                mGetToolListPrices,
                tabInfoStorer,
                handle) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return super.create(modelClass, extras)
    }

}