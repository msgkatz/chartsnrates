package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.msgkatz.ratesapp.data.entities.rest.Asset
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.domain.interactors.GetAssets
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo
import com.msgkatz.ratesapp.domain.interactors.GetQuoteAssetsMap
import com.msgkatz.ratesapp.domain.interactors.GetToolListPrices
import com.msgkatz.ratesapp.domain.interactors.base.Optional
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer
import com.msgkatz.ratesapp.presentation.ui.main.QuoteAssetPresenter
import com.msgkatz.ratesapp.presentation.ui.splash.SplashViewModel
import com.msgkatz.ratesapp.utils.Logs
import com.msgkatz.ratesapp.utils.Parameters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuoteAssetViewModel @Inject constructor(
    //TODO decibe regarding extra quoteAssetName
    private val _quoteAssetName: String?,
    private val mGetQuoteAssetsMap: GetQuoteAssetsMap,
    private val mGetToolListPrices: GetToolListPrices,
    private val tabInfoStorer: TabInfoStorer,
    private val handle: SavedStateHandle? = null
): ViewModel() {

    companion object {
        private val TAG: String = QuoteAssetViewModel::class.java.simpleName
    }

    // UI state exposed to the UI
    private val _quoteAssetUiState = MutableStateFlow<QuoteAssetUIState>(QuoteAssetUIState.Loading)
    val quoteAssetUiState: StateFlow<QuoteAssetUIState> = _quoteAssetUiState

    private val _priceListUiState = MutableStateFlow<PriceListUIState>(PriceListUIState.Loading)
    val priceListUiState: StateFlow<PriceListUIState> = _priceListUiState

    private var observerToolListPrices: ResponseObserver<Optional<Map<String, Set<PriceSimple>>>, Map<String, Set<PriceSimple>>>? = null
    private var observerQuoteAssets: ResponseObserver<Optional<Map<String, Asset>>, Map<String, Asset>>? = null
    private var quoteAsset: Asset? = null

    private val quoteAssetName: String? = handle?.get("quoteAssetName")

    init {
        onStart()
    }

    fun onStart() {
        observerQuoteAssets = object : ResponseObserver<Optional<Map<String, Asset>>, Map<String, Asset>>() {
            override fun doNext(stringAssetMap: Map<String, Asset>?) {
                quoteAsset = stringAssetMap?.get(quoteAssetName)

                updateQuoteAsset(quoteAsset)
                initToolListPrices()
            }

        }

        viewModelScope.launch {
            if (quoteAsset == null) {
                mGetQuoteAssetsMap.execute(observerQuoteAssets, null)
            } else {
                initToolListPrices()
            }
        }
    }

    fun onStop() {
        viewModelScope.launch {
            if (observerQuoteAssets != null) {
                observerQuoteAssets?.dispose(); observerQuoteAssets = null
            }
            if (observerToolListPrices != null) {
                observerToolListPrices?.dispose(); observerToolListPrices = null
            }
        }
    }

    private fun updateQuoteAsset(quoteAset: Asset?) {
        viewModelScope.launch {
            if (quoteAset != null)
                _quoteAssetUiState.value = QuoteAssetUIState.Data(quoteAset)
            else
                _quoteAssetUiState.value = QuoteAssetUIState.Empty
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    private fun updatePriceList(list: List<PriceSimple>?) {
        viewModelScope.launch {
            if (list != null)
                _priceListUiState.value = PriceListUIState.PriceList(
                    list,
                    placeholder(tabInfoStorer.getSmallDrawableByQuoteAssetName(quoteAssetName))
                )
            else
                _priceListUiState.value = PriceListUIState.Empty
        }
    }

    private fun initToolListPrices() {
        observerToolListPrices = object : ResponseObserver<Optional<Map<String, Set<PriceSimple>>>, Map<String, Set<PriceSimple>>>() {
            override fun doNext(stringSetMap: Map<String, Set<PriceSimple>>) {
                val prices = stringSetMap[quoteAsset!!.nameShort]!!
                val priceSimpleList: List<PriceSimple> = ArrayList(prices)

                updatePriceList(priceSimpleList)

                if (Parameters.DEBUG) {
                    val sb = StringBuilder(quoteAsset!!.nameShort)
                    sb.append("::")
                    for (item in prices) {
                        sb.append(item.toString())
                        sb.append("__")
                    }
                    Logs.d(TAG, sb.toString() + "\n")
                    Logs.d(TAG, "++++===============++++" + "\n")
                }
            }
        }


        viewModelScope.launch {
            mGetToolListPrices.execute(observerToolListPrices, null)
        }
    }

    override fun onCleared() {
        onStop()
        super.onCleared()
    }


    fun interface Factory {
        operator fun invoke(quoteAssetName: String?, mGetQuoteAssetsMap: GetQuoteAssetsMap, mGetToolListPrices: GetToolListPrices, tabInfoStorer: TabInfoStorer): QuoteAssetViewModel
    }

}

sealed interface QuoteAssetUIState {
    data object Loading : QuoteAssetUIState
    data object Empty : QuoteAssetUIState
    data class Data(val quoteAsset: Asset) : QuoteAssetUIState
}

sealed interface PriceListUIState {
    data object Loading : PriceListUIState
    data object Empty : PriceListUIState
    data class PriceList @OptIn(ExperimentalGlideComposeApi::class) constructor(val priceList: List<PriceSimple>, var placeHolder: Placeholder) : PriceListUIState
}