package com.msgkatz.ratesapp.presentation.ui.main.widget

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.presentation.ui.app.InterimVMKeeper

//const val LINKED_NEWS_RESOURCE_ID = "linkedNewsResourceId"
//const val forYouNavigationRoute = "for_you_route/{$LINKED_NEWS_RESOURCE_ID}"
//private const val DEEP_LINK_URI_PATTERN_OLD =
//    "https://www.nowinandroid.apps.samples.google.com/foryou/{$LINKED_NEWS_RESOURCE_ID}"

const val QUOTE_ASSET_NAME = "quoteAssetName"
const val quoteAssetNavigationRoute = "quote_asset_route"
const val quoteAssetNavigationRouteWithArgs = "quote_asset_route/{$QUOTE_ASSET_NAME}"
private const val DEEP_LINK_URI_PATTERN =
    "https://www.chartsnrates.apps.ratesapp.msgkatz.com/quote_asset/{$QUOTE_ASSET_NAME}"




fun NavController.navigateToQuoteAsset(quoteAssetName: String, navOptions: NavOptions? = null) {
    val route = ("$quoteAssetNavigationRoute/$quoteAssetName")
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.quoteAssetNavScreen(
    interimVMKeeper : InterimVMKeeper,
    onPriceItemClick: (PriceSimple) -> Unit,
) {
    composable(
        route = quoteAssetNavigationRouteWithArgs,
        deepLinks = listOf(
            navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN },
        ),
        arguments = listOf(
            navArgument(QUOTE_ASSET_NAME) { type = NavType.StringType },
        ),
    ) {navBackStackEntry ->
        val quoteAssetName = navBackStackEntry.arguments?.getString(QUOTE_ASSET_NAME)
        QuoteAssetRoute(
            quoteAssetName = quoteAssetName,
            interimVMKeeper = interimVMKeeper,
            onPriceItemClick = onPriceItemClick,
        )
    }
}