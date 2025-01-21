package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.msgkatz.ratesapp.old.domain.interactors.GetAssets
import com.msgkatz.ratesapp.old.domain.interactors.GetPlatformInfo
import com.msgkatz.ratesapp.old.domain.interactors.GetQuoteAssetsMap
import com.msgkatz.ratesapp.old.domain.interactors.GetToolListPrices
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer
import javax.inject.Inject

class MainActivityNewViewModel @Inject constructor(
    /** splash use-cases **/
    val mGetAssets: GetAssets,
    val mGetPlatformInfo: GetPlatformInfo,
    /** splash use-cases **/
    //private val quoteAssetName: String?,
    val mGetQuoteAssetsMap: GetQuoteAssetsMap,
    val mGetToolListPrices: GetToolListPrices,
    val tabInfoStorer: TabInfoStorer
) : ViewModel() {


    class Factory(
        val mGetAssets: GetAssets,
        val mGetPlatformInfo: GetPlatformInfo,
        /** splash use-cases **/
        //private val quoteAssetName: String?,
        val mGetQuoteAssetsMap: GetQuoteAssetsMap,
        val mGetToolListPrices: GetToolListPrices,
        val tabInfoStorer: TabInfoStorer
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityNewViewModel(
                mGetAssets,
                mGetPlatformInfo,
                mGetQuoteAssetsMap,
                mGetToolListPrices,
                tabInfoStorer) as T
        }
    }

    companion object {
        private val TAG: String = MainActivityNewViewModel::class.java.simpleName
    }

}