package com.msgkatz.ratesapp.presentation.ui.splash2

import com.msgkatz.ratesapp.data.model.PlatformInfo

interface SplashDataKeeper {
    suspend fun getPlatformInfo(): PlatformInfo?
}