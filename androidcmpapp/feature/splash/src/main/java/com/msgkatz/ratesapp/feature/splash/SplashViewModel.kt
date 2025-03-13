package com.msgkatz.ratesapp.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val tmpDataKeeper: SplashDataKeeper,
): ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(SplashUIState(loading = true))
    val uiState: StateFlow<SplashUIState> = _uiState

    init {
        loadAssets()
    }

    private fun loadAssets() = viewModelScope.launch(Dispatchers.IO) {
        if (tmpDataKeeper.getPlatformInfo() != null) {
            _uiState.update { it.copy(loading = false, errorLoading = false, loaded = true) }
        } else {
            _uiState.update { it.copy(loading = false, errorLoading = true) }
        }
    }

    fun tryReconnect() {
        _uiState.update { it.copy(loading = true, errorLoading = false) }
        loadAssets()
    }

    fun interface Factory {
        operator fun invoke(): SplashViewModel
    }

}


data class SplashUIState (
    val loading: Boolean = false,
    val errorLoading: Boolean = false,
    val loaded: Boolean = false,
)