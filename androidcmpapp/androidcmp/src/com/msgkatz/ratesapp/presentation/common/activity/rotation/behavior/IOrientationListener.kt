package com.msgkatz.ratesapp.presentation.common.activity.rotation.behavior


interface IOrientationListener {
    fun setOrientation(orientation: Orientation, lock: Boolean, isFullscreen: Boolean)
}