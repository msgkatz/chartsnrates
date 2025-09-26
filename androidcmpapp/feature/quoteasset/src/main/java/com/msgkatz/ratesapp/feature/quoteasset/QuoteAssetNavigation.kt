package com.msgkatz.ratesapp.feature.quoteasset

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.msgkatz.ratesapp.data.model.PriceSimple


const val QUOTE_ASSET_NAME = "quoteAssetName"
const val quoteAssetNavigationRoute = "quote_asset_route"
const val quoteAssetNavigationRouteWithArgs = "quote_asset_route/{$QUOTE_ASSET_NAME}"
private const val DEEP_LINK_URI_PATTERN =
    "https://www.chartsnrates.apps.ratesapp.msgkatz.com/quote_asset/{$QUOTE_ASSET_NAME}"



//TODO naming fixes
fun NavController.navigateToQuoteAsset(quoteAssetName: String, navOptions: NavOptions? = null) {
    val route = quoteAssetName
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.quoteAssetNavScreen(
    route: String,
    interimVMKeeper : QuoteAssetKeeper,
    onPriceItemClick: (PriceSimple) -> Unit,
    navController: NavController,
) {
    composable(
        //TODO clean
        route = route,
    ) { navBackStackEntry ->
        val parentEntry = remember(navBackStackEntry) {
            navController.getBackStackEntry(route)
        }
        QuoteAssetRoute(
            quoteAssetName = route,
            interimVMKeeper = interimVMKeeper,
            onPriceItemClick = onPriceItemClick,
            owner = parentEntry,
        )
    }
}