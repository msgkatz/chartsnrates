package com.msgkatz.ratesapp.feature.rootkmp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

import com.msgkatz.ratesapp.feature.rootkmp.main.MainIFace
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashIFace
//import com.msgkatz.ratesapp.feature.quoteassetkmp.MainIFace
//import com.msgkatz.ratesapp.feature.splashkmp.SplashIFace

interface RootIFace {

    val childStack: Value<ChildStack<*, Child>>

    fun navigateToMain()

    sealed class Child {
        data class Splash(val component: SplashIFace) : Child()
        data class Main(val component: MainIFace) : Child()

    }
}