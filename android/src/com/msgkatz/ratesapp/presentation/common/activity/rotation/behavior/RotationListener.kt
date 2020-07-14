package com.msgkatz.ratesapp.presentation.common.activity.rotation.behavior

import android.content.Context
import android.view.OrientationEventListener


internal abstract class RotationListener(context: Context?, rate: Int) : OrientationEventListener(context, rate) {
    private var currentOrientation: Orientation? = null

    override fun onOrientationChanged(orientation: Int) {
        val orient = orientationFromAngle(orientation) ?: return
        if( currentOrientation == null ) {
            currentOrientation = orient
            return
        }
        if( currentOrientation != null && orient != currentOrientation ) {
            currentOrientation = orient
            onRotationChanged(currentOrientation!!)
        }
    }

    private fun orientationFromAngle(angle: Int): Orientation? {
        return when {
            angle < 0 -> null
            angle > 318 || angle < 42 -> Orientation.ROTATION_0
            angle > 48 && angle < 132 -> Orientation.ROTATION_90
            angle > 138 && angle < 222 -> Orientation.ROTATION_180
            angle > 228 && angle < 312 -> Orientation.ROTATION_270
            else -> null
        }
    }

    abstract fun onRotationChanged(orientation: Orientation)
}