package com.msgkatz.ratesapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.badoo.reaktive.coroutinesinterop.asScheduler
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.msgkatz.ratesapp.core.uikitkmp.theme.CnrThemeAlter
import com.msgkatz.ratesapp.feature.rootkmp.RootComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.MainComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetArgs
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetDataKeeper
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashComponent
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashDataKeeper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * to run app use ./gradlew :androidcmpapp:kmpappfull:desktop:run
 */
fun main() {
    overrideSchedulers(main = Dispatchers.Main::asScheduler)
    val lifecycle = LifecycleRegistry()

    application {
        val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        val keeper = TmpDataKeeper(coroutineScope = null, //coroutineScope,
            ioDispatcher = ioDispatcher
        )
        val splash = { childContext: ComponentContext -> SplashComponent(childContext,
            keeper as SplashDataKeeper)
        }

        val qacmp = { childContext: ComponentContext, string: String ->
            QuoteAssetComponent(
                componentContext = childContext,
                quoteAssetArgs = QuoteAssetArgs(string),
                keeper as QuoteAssetDataKeeper
            )
        }

        val main = { childContext: ComponentContext ->
            MainComponent(
                componentContext = childContext,
                qacmp
            )
        }

        val root = remember { RootComponent(DefaultComponentContext(lifecycle = lifecycle), splash, main) }

        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Charts-n-Rates"
        ) {
            CnrThemeAlter(
                darkTheme = true,
                androidTheme = false,
                disableDynamicTheming = true
            ) {
                CnrApp(root)
            }
        }
    }
}