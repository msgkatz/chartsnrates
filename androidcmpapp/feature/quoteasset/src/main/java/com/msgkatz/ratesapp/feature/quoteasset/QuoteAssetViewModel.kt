package com.msgkatz.ratesapp.feature.quoteasset

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PriceSimple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuoteAssetViewModel @Inject constructor(
    private val tabInfoStorer: QuoteAssetDrawableDataKeeper, //TabInfoStorer,
    private val tmpDataKeeper: QuoteAssetDataKeeper,
    private val handle: SavedStateHandle? = null
): ViewModel() {

    companion object {
        private val TAG: String = "QuoteAssetViewModel"
        const val DEBUG = false
    }

    // UI state exposed to the UI
    private val _quoteAssetUiState = MutableStateFlow<QuoteAssetUIState>(QuoteAssetUIState.Loading)
    val quoteAssetUiState: StateFlow<QuoteAssetUIState> = _quoteAssetUiState

    private val _priceListUiState = MutableStateFlow<PriceListUIState>(PriceListUIState.Loading)
    val priceListUiState: StateFlow<PriceListUIState> = _priceListUiState

    private var quoteAsset: Asset? = null
    private val quoteAssetName: String? = handle?.get("quoteAssetName")


    fun onStart() {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (quoteAsset == null) {
                    tmpDataKeeper.getQuoteAssetMap()?.let { map ->
                        quoteAsset = map[quoteAssetName]
                        withContext(Dispatchers.Main) {
                            updateQuoteAsset(quoteAsset)
                        }

                    }
                }
                if (quoteAsset != null)
                    merge(
                        flowOf(tmpDataKeeper.getToolPrices()),
                        tmpDataKeeper.subscribeToolPrices()
                    )
                        .collect {
                            val prices = it[quoteAsset?.nameShort]
                            prices?.let { priceSet ->
                                val priceSimpleList = priceSet.toList()
                                println("listlen = ${priceSimpleList.size}")
                                withContext(Dispatchers.Main) {
                                    updatePriceList(priceSimpleList)
                                }
                            }

                            if (DEBUG) {
                                println("${TAG} :: ${quoteAsset?.nameShort}::${prices?.size ?: 0}")
                            }
                        }

            }
        }


    }

    fun onStop() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    private fun updateQuoteAsset(quoteAsset: Asset?) {
        viewModelScope.launch {
            if (quoteAsset != null)
                _quoteAssetUiState.value = QuoteAssetUIState.Data(quoteAsset)
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

    override fun onCleared() {
        onStop()
        super.onCleared()
    }

    fun interface Factory {
        operator fun invoke(quoteAssetName: String?, tabInfoStorer: QuoteAssetDrawableDataKeeper): QuoteAssetViewModel
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