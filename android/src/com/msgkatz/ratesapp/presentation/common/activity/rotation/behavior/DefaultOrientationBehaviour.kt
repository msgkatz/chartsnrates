package com.msgkatz.ratesapp.presentation.common.activity.rotation.behavior

import android.content.Context


class DefaultOrientationBehaviour(context: Context, orientationListener: IOrientationListener)
    : BaseOrientationBehaviour(context, orientationListener) {

    override fun onActivityConfigurationChanged(width: Int, height: Int, rotation: Orientation, multiWindowMode: Boolean): Boolean {
        log("onActivityConfigurationChanged($rotation, $multiWindowMode)")
        super.onActivityConfigurationChanged(width, height, rotation, multiWindowMode)
        currentOrientation = rotation
        isFullscreen = allowFullscreen && 0.8 * width > height
        return isFullscreen
    }

    override fun exitFullscreen(allowUnlockAfterRotate: Boolean) {
        log("exitFullscreen() isFullscreen $isFullscreen -> false")
        goToOrientation(Orientation.ROTATION_0, true, allowUnlockAfterRotate, false)
    }

    override fun goFullscreen() {
        log("goFullscreen")
        val isLandscape = currentOrientation == Orientation.ROTATION_90 || currentOrientation == Orientation.ROTATION_270
        val newOrientation = if( ! isLandscape ) Orientation.ROTATION_270 else currentOrientation ?: Orientation.ROTATION_270
        goToOrientation(newOrientation, true, true, true)
    }
}