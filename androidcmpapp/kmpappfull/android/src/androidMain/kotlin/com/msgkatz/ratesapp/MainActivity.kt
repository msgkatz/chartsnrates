package com.msgkatz.ratesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.retainedComponent
import com.msgkatz.ratesapp.core.uikit.theme.CnrThemeAlter
import com.msgkatz.ratesapp.feature.rootkmp.RootComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.MainComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetArgs
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetComponent
import com.msgkatz.ratesapp.feature.rootkmp.main.QuoteAssetDataKeeper
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashComponent
import com.msgkatz.ratesapp.feature.rootkmp.splash.SplashDataKeeper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        val root = RootComponent(defaultComponentContext(), splash, main)



        setContent {
            CnrThemeAlter(
                darkTheme = true,
                androidTheme = false, //shouldUseAndroidTheme(uiState),
                disableDynamicTheming = true //shouldDisableDynamicTheming(uiState),
            ) {
                CnrApp(root)
            }
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}