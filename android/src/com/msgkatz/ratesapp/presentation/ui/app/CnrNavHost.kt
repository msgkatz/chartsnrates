package com.msgkatz.ratesapp.presentation.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.presentation.ui.main.widget.quoteAssetNavScreen
import com.msgkatz.ratesapp.presentation.ui.splash.splashNavScreen
import com.msgkatz.ratesapp.presentation.ui.splash.splashNavigationRoute


/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun CnrNavHost(
    appState: CnrAppState,
    //onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = splashNavigationRoute,
    interimVMKeeper: InterimVMKeeper,
    onPriceItemClick: (PriceSimple) -> Unit,
    onContinue: () -> Unit = {},
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        splashNavScreen(interimVMKeeper, onContinue)
        quoteAssetNavScreen(interimVMKeeper, onPriceItemClick)
        quoteAssetNavScreen(interimVMKeeper, onPriceItemClick)
    }
}