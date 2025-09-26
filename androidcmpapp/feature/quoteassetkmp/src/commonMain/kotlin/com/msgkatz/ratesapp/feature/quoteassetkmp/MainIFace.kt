package com.msgkatz.ratesapp.feature.quoteassetkmp

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface MainIFace {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        //data class Splash(val component: SplashComponent) : Child()
        //data class Main(val component: MainComponent) : Child()

    }
}