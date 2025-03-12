package com.msgkatz.ratesapp.feature.common.activity.rotation.behavior

import android.content.Context
import android.hardware.SensorManager
import android.provider.Settings
import android.view.OrientationEventListener
//import io.reactivex.Observable
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


abstract class BaseOrientationBehaviour(
        val context: Context,
        private val orientationListener: IOrientationListener
) {
    private var sensorListener: OrientationEventListener? = null

    private var timerJob: Job? = null
    //private var pendingOrientationUnlock: Disposable? = null

    protected var currentOrientation: Orientation? = null
    protected var isFullscreen: Boolean = false
    protected var allowOrientationUnlockAfterRotate = false

    /**
     * Если поставить в true, то произойдет принудительный выход из фуллскрина. Запретит активити переворачиваться ватоматически.
     */
    open var allowFullscreen = false
        set(value) {
            if( field == value ) return
            field = value
            if( allowFullscreen ) {
                val isFullscreen = onActivityConfigurationChanged(lastWidth ?: return, lastHeight ?: return, currentOrientation ?: return, lastMultiWindowMode)
                goToOrientation(currentOrientation ?: return, false, true, isFullscreen)
            } else {
                exitFullscreen(false)
            }
        }

    init {
        sensorListener = object: RotationListener(context, SensorManager.SENSOR_DELAY_NORMAL) {
            override fun onRotationChanged(orientation: Orientation) {
                log("RotationListener onOrientationChanged orientation = $orientation")
                if( ! isUnlockPointless() && allowOrientationUnlockAfterRotate && currentOrientation != null ) {
                    //Нужно подождать, чтобы активити не перевернулась в старое состояние
                    startTimer(orientation)
//                    pendingOrientationUnlock = Observable
//                            .timer(1, TimeUnit.SECONDS)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribeOn(AndroidSchedulers.mainThread())
//                            .subscribe({
//                                log("Unlocking orientation orientation = $orientation")
//                                goToOrientation(currentOrientation!!, false, false, isFullscreen)
//                            })
                }
            }
        }
    }

    protected fun goToOrientation(orientation: Orientation, needLock: Boolean, allowUnlockAfterSensorChange: Boolean, isFullscreen: Boolean) {
        log("isFullscreen() isFullscreen ${this.isFullscreen} -> $isFullscreen")
        this.isFullscreen = isFullscreen
        allowOrientationUnlockAfterRotate = allowUnlockAfterSensorChange && needLock
        orientationListener.setOrientation(orientation, needLock, isFullscreen)

        stopTimer()
//        pendingOrientationUnlock?.dispose()
//        pendingOrientationUnlock = null

    }

    private var lastWidth: Int? = null
    private var lastHeight: Int? = null
    private var lastMultiWindowMode = false
    /**
     * @return is fullscreen
     */
    open fun onActivityConfigurationChanged(width: Int, height: Int, rotation: Orientation, multiWindowMode: Boolean): Boolean {
        lastWidth = width
        lastHeight = height
        lastMultiWindowMode = multiWindowMode
        return isFullscreen
    }

    fun onActivityStart() {
        sensorListener?.enable()
    }

    fun onActivityStop() {
        sensorListener?.disable()
    }

    /**
     * @return is succesfully exited
     */
    abstract fun exitFullscreen(allowUnlockAfterRotate: Boolean)

    protected abstract fun goFullscreen()

    fun toggleFullscreen() {
        if( isFullscreen ) {
            exitFullscreen(true)
        } else {
            goFullscreen()
        }
    }

    private fun isUnlockPointless(): Boolean {
        return Settings.System.getInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 0
    }

    @Suppress("UNUSED_PARAMETER")
    fun log(str: String) {
        //Functions.log("${this::class.simpleName}: $str")
    }

    /**
     * @return true if event consumed
     */
    fun onBackPressed(): Boolean {
        if( isFullscreen ) {
            exitFullscreen(true)
            return true
        }
        return false
    }



    private fun startTimer(orientation: Orientation) {
        timerJob?.cancel() // Cancel any existing timer

        timerJob = CoroutineScope(Dispatchers.Main).launch { // Using Main dispatcher for UI updates
            interval(1.seconds)
                .onEach {
                    log("Unlocking orientation orientation = $orientation")
                    goToOrientation(currentOrientation!!, false, false, isFullscreen)
                }
                .launchIn(this) // Launch the flow within this coroutine scope
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun interval(period: Duration, initialDelay: Duration = 0.seconds): Flow<Unit> = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

}