package com.msgkatz.ratesapp.presentation.ui.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msgkatz.ratesapp.data.entities.rest.Asset
import com.msgkatz.ratesapp.domain.entities.PlatformInfo
import com.msgkatz.ratesapp.domain.interactors.GetAssets
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo
import com.msgkatz.ratesapp.domain.interactors.base.Optional
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver
import com.msgkatz.ratesapp.presentation.ui.main.MainActivity
import com.msgkatz.ratesapp.utils.Logs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val mGetAssets: GetAssets,
    private val mGetPlatformInfo: GetPlatformInfo
): ViewModel() {

    companion object {
        private val MAX_COUNT = 1
    }

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(SplashUIState(loading = true))
    val uiState: StateFlow<SplashUIState> = _uiState

    init {
        loadAssets()
    }

    private val mHandler = Handler(Looper.myLooper()!!)
    private var counter = 0

    private fun loadAssets() {
        viewModelScope.launch {
            mGetAssets.execute(object :
                ResponseObserver<Optional<Map<String?, Asset?>?>?, Map<String?, Asset?>?>() {
                override fun doNext(stringAssetMap: Map<String?, Asset?>?) {
                    if (stringAssetMap != null) mHandler.post {
                        counter = 0
                        loadPlatformInfo()
                    }
                }

                override fun onError(exception: Throwable) {
                    super.onError(exception)

                    counter++
                    if (counter < SplashActivity.MAX_COUNT) mHandler.postDelayed(
                        { loadAssets() }, 50
                    )
                    else {
                        Logs.e(this, counter.toString() + " " + exception.message)
                        initErrorMessage()
                    }
                }


            }, null)
        }
    }

    /** step Two  */
    private fun loadPlatformInfo() {
        viewModelScope.launch {
            mGetPlatformInfo.execute(object :
                ResponseObserver<Optional<PlatformInfo?>?, PlatformInfo?>() {
                override fun doNext(platformInfo: PlatformInfo?) {
                    if (platformInfo != null) mHandler.post { initUI() }
                }

                override fun onError(exception: Throwable) {
                    super.onError(exception)

                    counter++
                    if (counter < SplashActivity.MAX_COUNT) mHandler.postDelayed(
                        { loadPlatformInfo() }, 50
                    )
                    else {
                        Logs.e(this, counter.toString() + " " + exception.message)
                        initErrorMessage()
                    }
                }
            }, null)
        }
    }

    public fun tryReconnect() {
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
//        binding.reconnect.setVisibility(View.VISIBLE)
//        binding.reconnect.setAlpha(0.0f)
//        binding.reconnect.animate().alpha(1.0f)
        }
    }

    private fun initUI() {
        viewModelScope.launch {
            _uiState.value = _uiState
                .value.copy(loading = false, errorLoading = false, loaded = true)
        }
        //showHomeActivity()
        //finish()
    }


}

data class SplashUIState(
    val loading: Boolean = false,
    val errorLoading: Boolean = false,
    val loaded: Boolean = false,
)