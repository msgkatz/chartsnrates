package com.msgkatz.ratesapp.feature.rootkmp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.msgkatz.ratesapp.feature.rootkmp.main.MainIFace
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashIFace
//import com.msgkatz.ratesapp.feature.quoteassetkmp.MainIFace
//import com.msgkatz.ratesapp.feature.splashkmp.SplashIFace
import kotlinx.serialization.Serializable

class RootComponent internal constructor(
    componentContext: ComponentContext,
    private val splash: (ComponentContext) -> SplashIFace,
    private val main: (ComponentContext) -> MainIFace
): RootIFace, ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()

    private val stack =
        childStack(
            source = navigation,
            initialConfiguration = Configuration.Splash,
            handleBackButton = true,
            childFactory = ::createChild,
            serializer = Configuration.serializer()
        )

    override val childStack: Value<ChildStack<*, RootIFace.Child>>
        get() = stack

    fun navigateToMain() {
        navigation.replaceAll(Configuration.Main)
    }

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): RootIFace.Child =
        when (configuration) {
            is Configuration.Main -> RootIFace.Child.Main(main(componentContext))
            is Configuration.Splash -> RootIFace.Child.Splash(splash(componentContext))
        }

    @Serializable
    private sealed class Configuration {
        @Serializable
        data object Splash : Configuration()

        @Serializable
        data object Main : Configuration()
    }
}