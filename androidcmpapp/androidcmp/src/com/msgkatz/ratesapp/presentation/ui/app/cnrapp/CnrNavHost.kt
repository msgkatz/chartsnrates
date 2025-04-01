package com.msgkatz.ratesapp.presentation.ui.app.cnrapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.msgkatz.ratesapp.data.model.PriceSimple
import com.msgkatz.ratesapp.presentation.ui.app.InterimVMKeeper
import com.msgkatz.ratesapp.feature.quoteasset.quoteAssetNavScreen


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
    modifier: Modifier = Modifier,
    destinations: List<CnrTopLevelDestination>,
    startDestination: String = "",
    interimVMKeeper: InterimVMKeeper,
    onPriceItemClick: (PriceSimple) -> Unit,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = destinations[0].route ?: startDestination,
        modifier = modifier,
    ) {
        destinations.forEach { it ->
            quoteAssetNavScreen(it.route, interimVMKeeper, onPriceItemClick, navController)
        }

    }
}