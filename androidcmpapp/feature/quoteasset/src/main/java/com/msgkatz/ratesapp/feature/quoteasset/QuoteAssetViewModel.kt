package com.msgkatz.ratesapp.feature.quoteasset

import androidx.compose.runtime.Immutable
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
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
        const val USE_FLOW_STATE = true
    }

    // UI state exposed to the UI
    private val _quoteAssetUiState = MutableStateFlow<QuoteAssetUIState>(QuoteAssetUIState.Loading)
    val quoteAssetUiState: StateFlow<QuoteAssetUIState> = _quoteAssetUiState

    private val _priceListUiState = MutableStateFlow<PriceListUIState>(PriceListUIState.Loading)
    val priceListUiState: StateFlow<PriceListUIState> = _priceListUiState

    private var quoteAsset: Asset? = null
    private val quoteAssetName: String? = handle?.get("quoteAssetName")


    fun onStart() {
        getPrices()
    }

    fun onStop() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    private fun getPrices() = viewModelScope.launch(Dispatchers.IO) {
        if (quoteAsset == null) {
            tmpDataKeeper.getQuoteAssetMap()?.let { map ->
                quoteAsset = map[quoteAssetName]
                withContext(Dispatchers.Main) {
                    updateQuoteAsset(quoteAsset)
                }

            }
        }
        if (quoteAsset != null && !USE_FLOW_STATE) {
            merge(
                flowOf(tmpDataKeeper.getToolPrices()),
                tmpDataKeeper.subscribeToolPrices()
            )
                .collect {
                    val prices = it[quoteAsset?.nameShort]
                    prices?.let { priceSet ->
                        val priceSimpleList = priceSet.toList()
                        if (DEBUG) { println("listlen = ${priceSimpleList.size}") }
                        withContext(Dispatchers.Main) {
                            updatePriceList(priceSimpleList)
                        }
                    }

                    if (DEBUG) {
                        println("${TAG} :: ${quoteAsset?.nameShort}::${prices?.size ?: 0}")
                    }
                }
        } else if (quoteAsset != null && USE_FLOW_STATE) {
            withContext(Dispatchers.Main) {
                updatePriceListFlow()
            }
        }

    }

    private fun updateQuoteAsset(quoteAsset: Asset?) {
        quoteAsset?.let {
            _quoteAssetUiState.update { QuoteAssetUIState.Data(quoteAsset) }
            return
        }

        _quoteAssetUiState.update { QuoteAssetUIState.Empty }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    private fun updatePriceList(list: List<PriceSimple>?) {
        list?.let {
            _priceListUiState.update {
                PriceListUIState.PriceList(
                    list,
                    placeholder(tabInfoStorer.getSmallDrawableByQuoteAssetName(quoteAssetName))
                )
            }
            return
        }

        _priceListUiState.update { PriceListUIState.Empty }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    private fun updatePriceListFlow() {
        val flow = flow<List<PriceSimple>> {
            val firstData = tmpDataKeeper.getToolPrices()
            firstData[quoteAsset?.nameShort]?.let { priceSet ->
                emit(priceSet.toList())
            }

            tmpDataKeeper.subscribeToolPrices().collect { it ->
                it[quoteAsset?.nameShort]?.let { priceSet ->
                    emit(priceSet.toList())
                }
            }
        }.flowOn(Dispatchers.IO)
        val data = PriceListData(flow = flow, placeHolder = placeholder(tabInfoStorer.getSmallDrawableByQuoteAssetName(quoteAssetName)))
        val state = PriceListUIState.PriceListFlow(data = data)
        _priceListUiState.value = state
    }

    override fun onCleared() {
        onStop()
        super.onCleared()
    }

    fun interface Factory {
        operator fun invoke(quoteAssetName: String?, tabInfoStorer: QuoteAssetDrawableDataKeeper): QuoteAssetViewModel
    }

}

@Immutable
sealed interface QuoteAssetUIState {
    data object Loading : QuoteAssetUIState
    data object Empty : QuoteAssetUIState
    data class Data(val quoteAsset: Asset) : QuoteAssetUIState
}

@Immutable
sealed interface PriceListUIState {
    data object Loading : PriceListUIState
    data object Empty : PriceListUIState
    data class PriceList @OptIn(ExperimentalGlideComposeApi::class) constructor(val priceList: List<PriceSimple>, val placeHolder: Placeholder) :
        PriceListUIState

    data class PriceListFlow constructor(val data: PriceListData): PriceListUIState

}

@OptIn(ExperimentalGlideComposeApi::class)
@Immutable
data class PriceListData(val flow: Flow<List<PriceSimple>>, val placeHolder: Placeholder)