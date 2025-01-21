package com.msgkatz.ratesapp.feature.common.activity.rotation.behavior

import android.content.Context

class TabletOrientationBehaviour(context: Context, orientationListener: IOrientationListener)
    : BaseOrientationBehaviour(context, orientationListener) {
    private var orientationUnlocked = false

    override fun onActivityConfigurationChanged(width: Int, height: Int, rotation: Orientation, multiWindowMode: Boolean): Boolean {
        log("onActivityConfigurationChanged($rotation, $multiWindowMode)")
        super.onActivityConfigurationChanged(width, height, rotation, multiWindowMode)
        currentOrientation = rotation
        if( orientationUnlocked == false ) {
            orientationUnlocked = true
            goToOrientation(rotation, false, true, isFullscreen )
        }
        return isFullscreen
    }

    override fun exitFullscreen(allowUnlockAfterRotate: Boolean) {
        log("exitFullscreen() isFullscreen $isFullscreen -> false")
        goToOrientation(currentOrientation ?: Orientation.ROTATION_0, false, true, false)
    }

    override fun goFullscreen() {
        goToOrientation(currentOrientation ?: Orientation.ROTATION_0, false, true, true)
    }
}