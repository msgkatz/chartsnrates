package com.msgkatz.ratesapp.presentation.ui.quoteasset2

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.msgkatz.ratesapp.data.entities.rest.AssetDT
import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PriceSimple
import com.msgkatz.ratesapp.domain.entities.PriceSimpleJava
import com.msgkatz.ratesapp.domain.interactors.GetQuoteAssetsMap
import com.msgkatz.ratesapp.domain.interactors.GetToolListPrices
import com.msgkatz.ratesapp.domain.interactors.base.Optional
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer
import com.msgkatz.ratesapp.presentation.ui.app.TmpDataKeeper

import com.msgkatz.ratesapp.utils.Logs
import com.msgkatz.ratesapp.utils.Parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuoteAssetViewModel @Inject constructor(
    //TODO decide regarding extra quoteAssetName
    private val _quoteAssetName: String?,
    private val mGetQuoteAssetsMap: GetQuoteAssetsMap,
    private val mGetToolListPrices: GetToolListPrices,
    private val tabInfoStorer: TabInfoStorer,
    private val tmpDataKeeper: TmpDataKeeper,
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

    private var observerToolListPrices: ResponseObserver<Optional<Map<String, Set<PriceSimpleJava>>>, Map<String, Set<PriceSimpleJava>>>? = null
    private var observerQuoteAssets: ResponseObserver<Optional<Map<String, AssetDT>>, Map<String, AssetDT>>? = null
    private var quoteAsset: Asset? = null

    private val quoteAssetName: String? = handle?.get("quoteAssetName")

    init {
        onStart()
    }

    fun onStart() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (quoteAsset == null) {
                    tmpDataKeeper.toolRepository.getQuoteAssetMap()?.let { map ->
                        quoteAsset = map[quoteAssetName]
                        withContext(Dispatchers.Main) {
                            updateQuoteAsset(quoteAsset)
                        }

                    }
                }
                if (quoteAsset != null)
                    tmpDataKeeper.toolListRealtimesPriceRepository
                        .subscribeToolPrices()
                        .collect {
                            val prices = it[quoteAsset?.nameShort]
                            prices?.let { priceSet ->
                                val priceSimpleList = priceSet.toList()
                                println("listlen = ${priceSimpleList.size}")
                                withContext(Dispatchers.Main) {
                                    updatePriceList(priceSimpleList)
                                }
                            }

                            if (Parameters.DEBUG) {
                                println("${TAG} :: ${quoteAsset?.nameShort}::${prices?.size ?: 0}")
//                                val sb = StringBuilder(quoteAsset!!.nameShort)
//                                sb.append("::")
//                                sb.append("::")
//                                for (item in prices) {
//                                    sb.append(item.toString())
//                                    sb.append("__")
//                                }
//                                Logs.d(TAG, sb.toString() + "\n")
//                                Logs.d(TAG, "++++===============++++" + "\n")
                            }
                        }

            }
        }

//        observerQuoteAssets = object : ResponseObserver<Optional<Map<String, AssetDT>>, Map<String, AssetDT>>() {
//            override fun doNext(stringAssetMap: Map<String, AssetDT>?) {
//                quoteAsset = stringAssetMap?.get(quoteAssetName)
//
//                updateQuoteAsset(quoteAsset)
//                initToolListPrices()
//            }
//
//        }
//
//
//        viewModelScope.launch {
//            if (quoteAsset == null) {
//                mGetQuoteAssetsMap.execute(observerQuoteAssets, null)
//            } else {
//                initToolListPrices()
//            }
//        }
    }

    fun onStop() {
        viewModelScope.coroutineContext.cancelChildren()
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

//    private fun initToolListPrices() {
//        observerToolListPrices = object : ResponseObserver<Optional<Map<String, Set<PriceSimpleJava>>>, Map<String, Set<PriceSimpleJava>>>() {
//            override fun doNext(stringSetMap: Map<String, Set<PriceSimpleJava>>) {
//                val prices = stringSetMap[quoteAsset!!.nameShort]!!
//                val priceSimpleList: List<PriceSimpleJava> = ArrayList(prices)
//
//                updatePriceList(priceSimpleList)
//
//                if (Parameters.DEBUG) {
//                    val sb = StringBuilder(quoteAsset!!.nameShort)
//                    sb.append("::")
//                    for (item in prices) {
//                        sb.append(item.toString())
//                        sb.append("__")
//                    }
//                    Logs.d(TAG, sb.toString() + "\n")
//                    Logs.d(TAG, "++++===============++++" + "\n")
//                }
//            }
//        }
//
//
//        viewModelScope.launch {
//            mGetToolListPrices.execute(observerToolListPrices, null)
//        }
//    }

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
    data class PriceList @OptIn(ExperimentalGlideComposeApi::class) constructor(val priceList: List<PriceSimple>, var placeHolder: Placeholder) :
        PriceListUIState
}