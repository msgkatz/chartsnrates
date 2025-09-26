package com.msgkatz.ratesapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.msgkatz.ratesapp.core.uikitkmp.theme.CnrThemeAlter
import com.msgkatz.ratesapp.feature.rootkmp.RootComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.MainComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetArgs
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetDataKeeper
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashComponent
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashDataKeeper
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.Document
import org.w3c.dom.get
import org.w3c.dom.set

/**
 * to run app use
 *             js: ./gradlew :androidcmpapp:kmpappfull:web:jsBrowserDevelopmentRun
 *                 ./gradlew :androidcmpapp:kmpappfull:web:jsBrowserRun
 *           wasm: ./gradlew :androidcmpapp:kmpappfull:web:wasmJsBrowserDevelopmentRun
 * to deploy wasm: ./gradlew :androidcmpapp:kmpappfull:web:wasmJsBrowserDistribution
 */

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()
    val stateKeeper = StateKeeperDispatcher(savedState = kotlinx.browser.localStorage[KEY_SAVED_STATE]?.decodeSerializableContainer())

    val ioDispatcher: CoroutineDispatcher = Dispatchers.Default //.IO
    val keeper = TmpDataKeeper(
        coroutineScope = null, //coroutineScope,
        ioDispatcher = ioDispatcher
    )
    val splash = { childContext: ComponentContext ->
        SplashComponent(
            childContext,
            keeper as SplashDataKeeper
        )
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

    lifecycle.attachToDocument()


    window.onbeforeunload =
        {
            //kotlinx.browser.localStorage.set(KEY_SAVED_STATE, stateKeeper.save().encodeToString)
            kotlinx.browser.localStorage[KEY_SAVED_STATE] = stateKeeper.save().encodeToString()
            null
        }
    CanvasBasedWindow(title = "Charts-n-Rates", canvasElementId = "ComposeTarget") {
        CnrThemeAlter(
            darkTheme = true,
            androidTheme = false,
            disableDynamicTheming = true
        ) {
            CnrApp(root)
        }
    }

}

private const val KEY_SAVED_STATE = "saved_state"

private fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        if (visibilityState(document) == "visible") {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    document.addEventListener(type = "visibilitychange", callback = { onVisibilityChanged() })
}

// Workaround for Document#visibilityState not available in Wasm
@JsFun("(document) => document.visibilityState")
private external fun visibilityState(document: Document): String

