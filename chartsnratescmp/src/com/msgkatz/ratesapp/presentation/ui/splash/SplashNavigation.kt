package com.msgkatz.ratesapp.presentation.ui.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.presentation.ui.app.InterimVMKeeper


const val splashNavigationRoute = "splash_route"
private const val DEEP_LINK_URI_PATTERN = "https://www.chartsnrates.apps.ratesapp.msgkatz.com/splash/"

fun NavGraphBuilder.splashNavScreen(
    interimVMKeeper : InterimVMKeeper,
    onContinue: () -> Unit = {},
) {
    composable(
        route = splashNavigationRoute,
        deepLinks = listOf(
            navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN },
        ),
    ) {
        SplashRoute(
            interimVMKeeper = interimVMKeeper,
            onContinue = onContinue,
        )
    }
}