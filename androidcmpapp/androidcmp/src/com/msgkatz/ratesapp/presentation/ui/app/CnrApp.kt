package com.msgkatz.ratesapp.presentation.ui.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.msgkatz.ratesapp.domain.entities.PriceSimple
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer
import com.msgkatz.ratesapp.presentation.theme.GradientColors
import com.msgkatz.ratesapp.presentation.theme.LocalGradientColors
import com.msgkatz.ratesapp.presentation.theme.component.CnrBackground
import com.msgkatz.ratesapp.presentation.theme.component.CnrGradientBackground

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class,
)
@Composable
fun CnrApp(
    //windowSizeClass: WindowSizeClass,
    tabInfoStorer: TabInfoStorer,
    onPriceItemClick: (PriceSimple) -> Unit,
    appState: CnrAppState = rememberCntAppState(
        tabInfoStorer = tabInfoStorer
    ),
    interimVMKeeper: InterimVMKeeper,
) {
    CnrAppContent(
        appState = appState,
        onPriceItemClick = onPriceItemClick,
        interimVMKeeper = interimVMKeeper,

    )
}

@Composable
fun CnrAppContent(
    modifier: Modifier = Modifier,
    appState: CnrAppState,
    onPriceItemClick: (PriceSimple) -> Unit,
    interimVMKeeper: InterimVMKeeper
) {
    val shouldShowGradientBackground = true

    CnrBackground {
        CnrGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            },
        ) {
            val topLevelDestination by appState.tabList.collectAsStateWithLifecycle()
            var showSplash by rememberSaveable { mutableStateOf(true) }
            Scaffold(
                modifier = modifier,
//                modifier = Modifier.semantics {
//                    testTagsAsResourceId = true
//                },
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                bottomBar = {
                    if (appState.shouldShowNavBar && !showSplash) {
                        CnrNavBar(
                            destinations = topLevelDestination,
                            onNavigateToDestination = appState::navigateToTopLevelDestination,

                            /** not in use **/
                            currentDestination = appState.currentDestination,
                            appState = appState,
                            modifier = Modifier.testTag("NiaBottomBar"),
                        )
                    }
                },
            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal,
                            ),
                        ),
                ) {
                    if (appState.shouldShowNavRail) {
                    }

                    Column(Modifier.fillMaxSize()) {
                        /**
                         * place for appbar
                         *
                         **/

                        CnrNavHost(
                            destinations = topLevelDestination,
                            appState = appState,
//                            onShowSnackbar = { message, action ->
//                                snackbarHostState.showSnackbar(
//                                    message = message,
//                                    actionLabel = action,
//                                    duration = SnackbarDuration.Short,
//                                ) == SnackbarResult.ActionPerformed
//                            }
                            interimVMKeeper = interimVMKeeper,
                            onPriceItemClick = onPriceItemClick,
                            onContinue = {
                                if (topLevelDestination.isNotEmpty())
                                    appState.navigateToTopLevelDestination(topLevelDestination[0])
                                showSplash = false
                            },

                        )
                    }
                }

            }

        }
    }

}


@Composable
fun CnrNavBar(
    destinations: List<CnrTopLevelDestination>,
    onNavigateToDestination: (CnrTopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    appState: CnrAppState,
    modifier: Modifier = Modifier,
) {
    val selectedDestination = remember {
        mutableStateOf(if (destinations.size > 0) destinations[0].route else "")
    }

    CnrNavigationBar(
        modifier = modifier,
    ) {

        destinations.forEach { destination ->
            CnrNavigationBarItem(
                selected = selectedDestination.value == destination.route,
                onClick = {
                    selectedDestination.value = destination.route
                    onNavigateToDestination(destination)
                          },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.unselectedIconId),
                        contentDescription = null,
                    )
                },
            )

        }
    }

}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: CnrTopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false



data class CnrTopLevelDestination(
    val route: String,
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val iconTextId: Int
)