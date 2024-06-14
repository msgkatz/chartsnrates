package com.msgkatz.ratesapp.presentation.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msgkatz.ratesapp.data.entities.rest.Asset
import com.msgkatz.ratesapp.domain.entities.PlatformInfo
import com.msgkatz.ratesapp.domain.interactors.GetAssets
import com.msgkatz.ratesapp.domain.interactors.GetPlatformInfo
import com.msgkatz.ratesapp.domain.interactors.base.Optional
import com.msgkatz.ratesapp.domain.interactors.base.ResponseObserver
import com.msgkatz.ratesapp.utils.Logs
import com.msgkatz.ratesapp.utils.asResult
import com.msgkatz.ratesapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
    val uiState2: StateFlow<SplashUIState> = _uiState

    init {
        loadAssets()
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private var counter = 0

    private fun loadAssets0() {
        viewModelScope.launch {
            mGetAssets.getAsFlow(null)
                .map { it ->
                    when (it?.get()) {
                        null -> {}
                        else -> {

                        }
                    }
                }
                .collect()
        }
    }
    private fun loadAssets() {
        viewModelScope.launch {
//            mGetAssets.getAsFlow(null)
////                .flatMapLatest {  it ->
////                    when (it?.get() == null) {
////                        true -> {
////                            initErrorMessage()
////                        }
////                        false -> {
////                            mGetPlatformInfo.getAsFlow(null)
////
////                    }
////                }
//                .asResult()
//                .map {
//                    when (it) {
//                        is Result.Loading -> {}
//                        is Result.Error -> { initErrorMessage() }
//                        is Result.Success -> {
//                            if (it.data?.get() == null) {
//                                initErrorMessage()
//                            } else {
//                                if (1==2) loadPlatformInfo()
//                                else
//                                mGetPlatformInfo.getAsFlow(null)
//                                    .asResult()
//                                    .map { it ->
//                                        when(it) {
//                                            is Result.Loading -> {}
//                                            is Result.Error -> { initErrorMessage() }
//                                            is Result.Success -> {  initUI() }
//                                        }
//                                    }.flowOn(Dispatchers.Default).collect()
//                            }
//                        }
//                    }
//
//                }.collect()
//        }

            combine(
                mGetAssets.getAsFlow(null),
                mGetPlatformInfo.getAsFlow(null),
                ::Pair
            ).asResult()
                //.retry(3)
                .map { it ->
                    when (it) {
                        is Result.Loading -> {
                            //SplashUIState.Loading
                        }

                        is Result.Success -> {
                            if (it.data.first?.get() != null && it.data.second?.get() != null) {
                                initUI()
                                //SplashUIState.Loaded
                            } else {
                                initErrorMessage()
                                //SplashUIState.Error
                            }
                        }

                        is Result.Error -> {
                            initErrorMessage()
                            //SplashUIState.Error
                        }
                    }
                }.flowOn(Dispatchers.Default).collect()
        }
    }

    private fun loadAssets2() {
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
                    if (counter < MAX_COUNT) mHandler.postDelayed(
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
                    if (counter < MAX_COUNT) mHandler.postDelayed(
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

    fun interface Factory {
        operator fun invoke(mGetAssets: GetAssets, mGetPlatformInfo: GetPlatformInfo): SplashViewModel
    }

}


data class SplashUIState (
    val loading: Boolean = false,
    val errorLoading: Boolean = false,
    val loaded: Boolean = false,
)