package com.msgkatz.ratesapp.feature.rootkmp.main

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.msgkatz.ratesapp.data.model.Asset
import com.msgkatz.ratesapp.data.model.PriceSimple
import com.msgkatz.ratesapp.feature.rootkmp.decompose.coroutineScope
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
import kotlinx.serialization.Serializable

class QuoteAssetComponent internal constructor(
    componentContext: ComponentContext,
    private val quoteAssetArgs: QuoteAssetArgs,
    private val tmpDataKeeper: QuoteAssetDataKeeper,
): QuoteAssetIFace, ComponentContext by componentContext {

    companion object {
        private val TAG: String = "QuoteAssetViewModel"
        const val DEBUG = false
    }

    private val viewModelScope = coroutineScope()

    // UI state exposed to the UI
    private val _quoteAssetUiState = MutableStateFlow<QuoteAssetUIState>(QuoteAssetUIState.Loading)
    override val quoteAssetUiState: StateFlow<QuoteAssetUIState> = _quoteAssetUiState

    private val _priceListUiState = MutableStateFlow<PriceListUIState>(PriceListUIState.Loading)
    override val priceListUiState: StateFlow<PriceListUIState> = _priceListUiState

    private var quoteAsset: Asset? = null
    private val quoteAssetName: String? = quoteAssetArgs.quoteAssetName

    init {
        lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onStart() {
                whenStart()

            }

            override fun onStop() {
                whenStop()
            }
        })
    }

    fun whenStart() {
        getPrices()
    }

    fun whenStop() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    private fun getPrices() = viewModelScope.launch(Dispatchers.Default) {
        if (quoteAsset == null) {
            tmpDataKeeper.getQuoteAssetMap()?.let { map ->
                quoteAsset = map[quoteAssetName]
                withContext(Dispatchers.Main) {
                    updateQuoteAsset(quoteAsset)
                }

            }
        }
        if (quoteAsset != null) {
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

    //@OptIn(ExperimentalGlideComposeApi::class)
    private fun updatePriceList(list: List<PriceSimple>?) {
        list?.let {
            _priceListUiState.update {
                PriceListUIState.PriceList(
                    priceList = list,
                    placeHolder = quoteAssetName ?: ""
                    //placeholder(tabInfoStorer.getSmallDrawableByQuoteAssetName(quoteAssetName))
                )
            }
            return
        }

        _priceListUiState.update { PriceListUIState.Empty }
    }

    //@OptIn(ExperimentalGlideComposeApi::class)
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
        }.flowOn(Dispatchers.Default)
        //val data = PriceListData(flow = flow, placeHolder = placeholder(tabInfoStorer.getSmallDrawableByQuoteAssetName(quoteAssetName)))
        val data = PriceListData(flow = flow, placeHolder = quoteAssetName ?: "")
        val state = PriceListUIState.PriceListFlow(data = data)
        _priceListUiState.value = state
    }

//    override fun onCleared() {
//        onStop()
//        super.onCleared()
//    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext,  quoteAssetArgs: QuoteAssetArgs): QuoteAssetComponent
    }
}

@Serializable
class QuoteAssetArgs(val quoteAssetName: String)

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
    data class PriceList
    //@OptIn(ExperimentalGlideComposeApi::class)
    constructor(val priceList: List<PriceSimple>, val placeHolder: String) :
        PriceListUIState

    data class PriceListFlow constructor(val data: PriceListData): PriceListUIState

}

//@OptIn(ExperimentalGlideComposeApi::class)
@Immutable
data class PriceListData(val flow: Flow<List<PriceSimple>>, val placeHolder: String)