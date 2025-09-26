package com.msgkatz.ratesapp.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootIFace {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class Splash(val component: SplashIFace) : Child()
        data class Main(val component: MainIFace) : Child()

    }
}

class SplashComponent internal constructor(
    componentContext: ComponentContext
): SplashIFace {}
interface SplashIFace {

    //val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        //data class Splash(val component: SplashComponent) : Child()
        //data class Main(val component: MainComponent) : Child()

    }
}

class MainComponent internal constructor(
    componentContext: ComponentContext
): MainIFace {
    override val childStack: Value<ChildStack<*, MainIFace.Child>>
        get() = TODO("Not yet implemented")
}
interface MainIFace {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        //data class Splash(val component: SplashComponent) : Child()
        //data class Main(val component: MainComponent) : Child()

    }
}