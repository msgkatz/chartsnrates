package com.msgkatz.ratesapp.feature.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink


const val splashNavigationRoute = "splash_route"
private const val DEEP_LINK_URI_PATTERN = "https://www.chartsnrates.apps.ratesapp.msgkatz.com/splash/"

fun NavGraphBuilder.splashNavScreen(
    interimVMKeeper : SplashKeeper,
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