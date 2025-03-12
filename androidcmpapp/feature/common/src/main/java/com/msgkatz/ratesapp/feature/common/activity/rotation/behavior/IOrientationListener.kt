package com.msgkatz.ratesapp.feature.common.activity.rotation.behavior


interface IOrientationListener {
    fun setOrientation(orientation: Orientation, lock: Boolean, isFullscreen: Boolean)
}