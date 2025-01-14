package com.msgkatz.ratesapp.feature.quoteasset

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.msgkatz.ratesapp.data.model.PriceSimple

//import com.msgkatz.ratesapp.presentation.ui.app.InterimVMKeeper

//const val LINKED_NEWS_RESOURCE_ID = "linkedNewsResourceId"
//const val forYouNavigationRoute = "for_you_route/{$LINKED_NEWS_RESOURCE_ID}"
//private const val DEEP_LINK_URI_PATTERN_OLD =
//    "https://www.nowinandroid.apps.samples.google.com/foryou/{$LINKED_NEWS_RESOURCE_ID}"

const val QUOTE_ASSET_NAME = "quoteAssetName"
const val quoteAssetNavigationRoute = "quote_asset_route"
const val quoteAssetNavigationRouteWithArgs = "quote_asset_route/{$QUOTE_ASSET_NAME}"
private const val DEEP_LINK_URI_PATTERN =
    "https://www.chartsnrates.apps.ratesapp.msgkatz.com/quote_asset/{$QUOTE_ASSET_NAME}"



//
//fun NavController.navigateToQuoteAsset(quoteAssetName: String, navOptions: NavOptions? = null) {
//    val route = ("$quoteAssetNavigationRoute/$quoteAssetName")
//    this.navigate(route, navOptions)
//}
//
//fun NavGraphBuilder.quoteAssetNavScreen(
//    interimVMKeeper : InterimVMKeeper,
//    onPriceItemClick: (PriceSimple) -> Unit,
//    navController: NavController,
//) {
//    composable(
//        route = quoteAssetNavigationRouteWithArgs,
//        deepLinks = listOf(
//            navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN },
//        ),
//        arguments = listOf(
//            navArgument(QUOTE_ASSET_NAME) { type = NavType.StringType },
//        ),
//    ) {navBackStackEntry ->
//        val quoteAssetName = navBackStackEntry.arguments?.getString(QUOTE_ASSET_NAME)
//        val entry = navBackStackEntry
//        val parentEntry = remember(navBackStackEntry) {
//            //navController.getBackStackEntry("parentNavigationRoute")
//            navController.getBackStackEntry(quoteAssetNavigationRouteWithArgs)
//
//        }
//        QuoteAssetRoute(
//            quoteAssetName = quoteAssetName,
//            interimVMKeeper = interimVMKeeper,
//            onPriceItemClick = onPriceItemClick,
//            owner = parentEntry,
//        )
//    }
//}

//TODO naming fixes
fun NavController.navigateToQuoteAsset2(quoteAssetName: String, navOptions: NavOptions? = null) {
    //val route = ("$quoteAssetNavigationRoute/$quoteAssetName")
    val route = quoteAssetName
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.quoteAssetNavScreen2(
    route: String, //as quoteAssetName
    interimVMKeeper : QuoteAssetKeeper,
    onPriceItemClick: (PriceSimple) -> Unit,
    navController: NavController,
) {
    composable(
        //TODO clean
        route = route, //quoteAssetNavigationRouteWithArgs,
//        deepLinks = listOf(
//            navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN },
//        ),
//        arguments = listOf(
//            navArgument(QUOTE_ASSET_NAME) { type = NavType.StringType },
//        ),
    ) {navBackStackEntry ->
//        val quoteAssetName = navBackStackEntry.arguments?.getString(QUOTE_ASSET_NAME)
//        val entry = navBackStackEntry
        val parentEntry = remember(navBackStackEntry) {
            //navController.getBackStackEntry(quoteAssetNavigationRouteWithArgs)
            navController.getBackStackEntry(route)
        }
        QuoteAssetRoute(
            quoteAssetName = route, //quoteAssetName,
            interimVMKeeper = interimVMKeeper,
            onPriceItemClick = onPriceItemClick,
            owner = parentEntry,
        )
    }
}