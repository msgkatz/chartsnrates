package com.msgkatz.ratesapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.msgkatz.ratesapp.feature.rootkmp.RootComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.MainComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetArgs
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetDataKeeper
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashComponent
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashDataKeeper
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * to run app use
 * js     ./gradlew :androidcmpapp:kmpappfull:web:jsBrowserDevelopmentRun
 * ./gradlew :androidcmpapp:kmpappfull:web:jsBrowserRun
 * wasm   ./gradlew :androidcmpapp:kmpappfull:web:wasmJsBrowserDevelopmentRun
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()

    val ioDispatcher: CoroutineDispatcher = Dispatchers.Default //.IO
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

    val root = RootComponent(DefaultComponentContext(lifecycle = lifecycle), splash, main)

    lifecycle.resume()

    ComposeViewport(document.body!!) {
        CnrApp(root)
    }
}