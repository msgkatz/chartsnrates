package com.msgkatz.ratesapp.feature.rootkmp.splash

import kotlinx.coroutines.flow.StateFlow

interface SplashIFace {

    //val childStack: Value<ChildStack<*, Child>>
    val uiState: StateFlow<SplashUIState>

    fun tryReconnect()

    sealed class Child {
        //data class Splash(val component: SplashComponent) : Child()
        //data class Main(val component: MainComponent) : Child()

    }
}