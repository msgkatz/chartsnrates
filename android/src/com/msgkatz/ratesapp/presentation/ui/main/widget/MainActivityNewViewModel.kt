package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.lifecycle.ViewModel
import com.msgkatz.ratesapp.domain.interactors.GetAssets
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo
import com.msgkatz.ratesapp.domain.interactors.GetQuoteAssetsMap
import com.msgkatz.ratesapp.domain.interactors.GetToolListPrices
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

    companion object {
        private val TAG: String = MainActivityNewViewModel::class.java.simpleName
    }

}