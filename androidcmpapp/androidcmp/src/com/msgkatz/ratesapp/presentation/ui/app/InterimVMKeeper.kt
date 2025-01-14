package com.msgkatz.ratesapp.presentation.ui.app

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras

import com.msgkatz.ratesapp.domain.interactors.GetAssets
import com.msgkatz.ratesapp.domain.interactors.GetIntervals
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo
import com.msgkatz.ratesapp.domain.interactors.GetQuoteAssetsMap
import com.msgkatz.ratesapp.domain.interactors.GetToolListPrices
import com.msgkatz.ratesapp.domain.interactors.GetTools
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer
import com.msgkatz.ratesapp.presentation.common.messaging.IRxBus
import com.msgkatz.ratesapp.presentation.ui.chart.widget.ChartParentViewModel

import com.msgkatz.ratesapp.feature.quoteasset.QuoteAssetKeeper
import com.msgkatz.ratesapp.feature.quoteasset.QuoteAssetViewModel as qavm

import com.msgkatz.ratesapp.feature.splash.SplashKeeper
import com.msgkatz.ratesapp.feature.splash.SplashViewModel as svm

class InterimVMKeeper(
    private val owner: ViewModelStoreOwner,
    private val mGetAssets: GetAssets,
    private val mGetPlatformInfo: GetPlatformInfo,
    private val mGetQuoteAssetsMap: GetQuoteAssetsMap,
    private val mGetToolListPrices: GetToolListPrices,
    private val tabInfoStorer: TabInfoStorer,
    private val mGetTools: GetTools? = null,
    private val mGetIntervals: GetIntervals? = null,
    private val rxBus: IRxBus? = null,
    private val tmpDataKeeper: TmpDataKeeper
): SplashKeeper, QuoteAssetKeeper {


    override fun makeSplash5(): svm {
        val viewModelFactory = SplashViewModelFactory2(mGetAssets, mGetPlatformInfo, tmpDataKeeper)
        val viewModel = ViewModelProvider(owner, viewModelFactory)[svm::class.java]
        return viewModel
    }

    override fun makeQuoteAsset5(quoteAssetName: String?, _owner: ViewModelStoreOwner): qavm {
        val viewModelFactory = QuoteAssetSavedStateViewModelFactory2(quoteAssetName, mGetQuoteAssetsMap, mGetToolListPrices, tabInfoStorer, tmpDataKeeper)
        val viewModel = ViewModelProvider(_owner, viewModelFactory)[qavm::class.java]
        return viewModel
    }


    fun makeChartParentViewModel(toolName: String, toolPrice: Double, _owner: ViewModelStoreOwner): ChartParentViewModel {
        val viewModelFactory = ChartParentViewModelFactory(toolName, toolPrice, mGetTools, mGetIntervals, rxBus)
        val viewModel = ViewModelProvider(_owner, viewModelFactory)[ChartParentViewModel::class.java]
        return viewModel
    }



}


class SplashViewModelFactory2(
    private val mGetAssets: GetAssets,
    private val mGetPlatformInfo: GetPlatformInfo,
    private val tmpDataKeeper: TmpDataKeeper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(svm::class.java)) {
            return svm(tmpDataKeeper) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}

@JvmField
val MY_ARGS_KEY = object : CreationExtras.Key<String> {}



class QuoteAssetSavedStateViewModelFactory2(private val quoteAssetName: String?,
                                            private val mGetQuoteAssetsMap: GetQuoteAssetsMap,
                                            private val mGetToolListPrices: GetToolListPrices,
                                            private val tabInfoStorer: TabInfoStorer,
                                            private val tmpDataKeeper: TmpDataKeeper
) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(qavm::class.java)){
            handle.set("quoteAssetName", quoteAssetName)
            return qavm(tabInfoStorer,
                tmpDataKeeper,
                handle) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return super.create(modelClass, extras)
    }

}

class ChartParentViewModelFactory(private val toolName: String,
                                  private val toolPrice: Double,
                                  private val mGetTools: GetTools?,
                                  private val mGetIntervals: GetIntervals?,
                                  private val rxBus: IRxBus?
) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(ChartParentViewModel::class.java)){
            handle.set("toolName", toolName)
            handle.set("toolPrice", toolPrice)
            return ChartParentViewModel(mGetTools!!,
                mGetIntervals!!,
                rxBus,
                //handle
            ) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return super.create(modelClass, extras)
    }

}