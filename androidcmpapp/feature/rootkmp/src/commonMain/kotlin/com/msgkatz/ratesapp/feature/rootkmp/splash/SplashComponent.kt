package com.msgkatz.ratesapp.feature.rootkmp.splash

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.msgkatz.ratesapp.feature.rootkmp.decompose.coroutineScope

//import com.msgkatz.ratesapp.feature.splashkmp.SplashIFace

class SplashComponent internal constructor(
    componentContext: ComponentContext,
    private val tmpDataKeeper: SplashDataKeeper,
): SplashIFace, ComponentContext by componentContext {

    private val viewModelScope = coroutineScope()

    private val _uiState = MutableStateFlow(SplashUIState(loading = true))
    override val uiState: StateFlow<SplashUIState> = _uiState

    init {
        loadAssets()
    }

    private fun loadAssets() = viewModelScope.launch(Dispatchers.Default) {
        if (tmpDataKeeper.getPlatformInfo() != null) {
            _uiState.update { it.copy(loading = false, errorLoading = false, loaded = true) }
        } else {
            _uiState.update { it.copy(loading = false, errorLoading = true) }
        }
    }

    override fun tryReconnect() {
        _uiState.update { it.copy(loading = true, errorLoading = false) }
        loadAssets()
    }

    fun interface Factory {
        operator fun invoke(): SplashComponent
    }
}

data class SplashUIState (
    val loading: Boolean = false,
    val errorLoading: Boolean = false,
    val loaded: Boolean = false,
)