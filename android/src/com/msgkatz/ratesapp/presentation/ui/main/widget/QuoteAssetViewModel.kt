package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msgkatz.ratesapp.R
import com.msgkatz.ratesapp.data.entities.rest.Asset
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.domain.interactors.GetQuoteAssetsMap
import com.msgkatz.ratesapp.domain.interactors.GetToolListPrices
import com.msgkatz.ratesapp.domain.interactors.base.Optional
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver
import com.msgkatz.ratesapp.presentation.ui.main.QuoteAssetPresenter
import com.msgkatz.ratesapp.utils.Logs
import com.msgkatz.ratesapp.utils.Parameters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class QuoteAssetViewModel @Inject constructor(
    private val quoteAssetName: String?,
    private val mGetQuoteAssetsMap: GetQuoteAssetsMap,
    private val mGetToolListPrices: GetToolListPrices
): ViewModel() {

    companion object {
        private val TAG: String = QuoteAssetPresenter::class.java.simpleName
    }

    // UI state exposed to the UI
    private val _quoteAssetUiState = MutableStateFlow<QuoteAssetUIState>(QuoteAssetUIState.Loading)
    val quoteAssetUiState: StateFlow<QuoteAssetUIState> = _quoteAssetUiState

    private val _priceListUiState = MutableStateFlow<PriceListUIState>(PriceListUIState.Loading)
    val priceListUiState: StateFlow<PriceListUIState> = _priceListUiState

    private var observerToolListPrices: ResponseObserver<Optional<Map<String, Set<PriceSimple>>>, Map<String, Set<PriceSimple>>>? = null
    private var observerQuoteAssets: ResponseObserver<Optional<Map<String, Asset>>, Map<String, Asset>>? = null
    private var quoteAsset: Asset? = null

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

    private fun updatePriceList(list: List<PriceSimple>?) {
        viewModelScope.launch {
            if (list != null)
                _priceListUiState.value = PriceListUIState.PriceList(list)
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

}

sealed interface QuoteAssetUIState {
    data object Loading : QuoteAssetUIState
    data object Empty : QuoteAssetUIState
    data class Data(val quoteAsset: Asset) : QuoteAssetUIState
}

sealed interface PriceListUIState {
    data object Loading : PriceListUIState
    data object Empty : PriceListUIState
    data class PriceList(val priceList: List<PriceSimple>) : PriceListUIState
}