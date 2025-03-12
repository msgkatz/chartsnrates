package com.msgkatz.ratesapp.feature.common.activity.rotation

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import com.msgkatz.ratesapp.feature.common.activity.rotation.behavior.BaseOrientationBehaviour
import com.msgkatz.ratesapp.feature.common.activity.rotation.behavior.DefaultOrientationBehaviour
import com.msgkatz.ratesapp.feature.common.activity.rotation.behavior.IOrientationListener
import com.msgkatz.ratesapp.feature.common.activity.rotation.behavior.Orientation

abstract class BaseRotationCompActivity : ComponentActivity(), IOrientationListener {
    protected var orientationBehaviour: BaseOrientationBehaviour? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orientationBehaviour = onCreateOrientationBehaviour()
    }

    override fun onBackPressed() {
        if( orientationBehaviour?.onBackPressed() == false ) {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        handleOrientationConfigurationChanged()
        orientationBehaviour?.onActivityStart()
    }

    override fun onStop() {
        super.onStop()
        orientationBehaviour?.onActivityStop()
    }

    override fun setOrientation(orientation: Orientation, lock: Boolean, isFullscreen: Boolean) {
        val newRequestedOrientation = when {
            ! lock -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            orientation == Orientation.ROTATION_0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            orientation == Orientation.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            orientation == Orientation.ROTATION_180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            orientation == Orientation.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else -> return
        }

        requestedOrientation = newRequestedOrientation
        setFullscreenMode(isFullscreen)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handleOrientationConfigurationChanged()
    }

    /**
     * @return isFullscreen
     */
    protected open fun handleOrientationConfigurationChanged() {
        val rotation = when(windowManager?.defaultDisplay?.rotation ?: Surface.ROTATION_0) {
            Surface.ROTATION_90 -> Orientation.ROTATION_270
            Surface.ROTATION_180 -> Orientation.ROTATION_180
            Surface.ROTATION_270 -> Orientation.ROTATION_90
            else -> Orientation.ROTATION_0
        }

        val point = Point()
        windowManager?.defaultDisplay?.getSize(point)
        val multiWindow = isInMultiWindowModeCompat()
        val isFullscreen = orientationBehaviour?.onActivityConfigurationChanged(point.x, point.y, rotation, multiWindow) ?: false
        setFullscreenMode( isFullscreen )
    }

    private var isInMultiWindowModeInner = false
    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean) {
        super.onMultiWindowModeChanged(isInMultiWindowMode)
        this.isInMultiWindowModeInner = isInMultiWindowMode
        handleOrientationConfigurationChanged()
    }
//    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration?) {
//        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig!!)
//        handleOrientationConfigurationChanged()
//    }

    private fun isInMultiWindowModeCompat(): Boolean {
        return if (Build.VERSION.SDK_INT >= 24) { ///Build.VERSION_CODES.N) {
            //isInMultiWindowMode
            isInMultiWindowModeInner
        } else {
            false
        }
    }

    abstract fun setFullscreenMode(isFullscreen: Boolean)
    open fun onCreateOrientationBehaviour(): BaseOrientationBehaviour {
        return DefaultOrientationBehaviour(applicationContext, this)
    }
}