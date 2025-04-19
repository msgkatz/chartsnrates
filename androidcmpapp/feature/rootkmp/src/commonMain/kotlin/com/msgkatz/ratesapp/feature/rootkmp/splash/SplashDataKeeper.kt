package com.msgkatz.ratesapp.feature.rootkmp.splash

import com.msgkatz.ratesapp.data.model.PlatformInfo

interface SplashDataKeeper {
    suspend fun getPlatformInfo(): PlatformInfo?
}