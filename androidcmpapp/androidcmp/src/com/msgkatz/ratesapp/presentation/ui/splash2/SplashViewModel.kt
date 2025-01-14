package com.msgkatz.ratesapp.presentation.ui.splash2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msgkatz.ratesapp.data.model.PlatformInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private fun loadAssets() {
        val job = viewModelScope.launch {
            var info: PlatformInfo? = null
            withContext(Dispatchers.IO) {
                info = tmpDataKeeper.getPlatformInfo()
            }
            if (info != null) {
                initUI()
            } else {
                initErrorMessage()
            }
        }

    }

    fun tryReconnect() {
        viewModelScope.launch {
            _uiState.value = _uiState
                .value.copy(loading = true, errorLoading = false)
            loadAssets()
        }

    }

    private fun initErrorMessage() {
        viewModelScope.launch {
            _uiState.value = _uiState
                .value.copy(loading = false, errorLoading = true)
        }
    }

    private fun initUI() {
        viewModelScope.launch {
            _uiState.value = _uiState
                .value.copy(loading = false, errorLoading = false, loaded = true)
        }

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