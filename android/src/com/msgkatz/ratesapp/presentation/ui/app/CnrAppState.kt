package com.msgkatz.ratesapp.presentation.ui.app

//import androidx.compose.material3.windowsizeclass.WindowSizeClass
//import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.util.trace
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.msgkatz.ratesapp.presentation.common.TabInfoStorer
import com.msgkatz.ratesapp.presentation.entities.TabItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


@Composable
fun rememberCntAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    //windowSizeClass: WindowSizeClass,
    tabInfoStorer: TabInfoStorer,
): CnrAppState {
    return remember(
        navController,
        coroutineScope,
        //windowSizeClass,
        tabInfoStorer
    ) {
        CnrAppState(
            navController,
            coroutineScope,
            //windowSizeClass,
            tabInfoStorer
        )
    }
}

@Stable
class CnrAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    //val windowSizeClass: WindowSizeClass,
    val tabInfoStorer: TabInfoStorer,
) {
    val shouldShowNavBar: Boolean
        get() = true
    val shouldShowNavRail: Boolean
        get() = !shouldShowNavBar

    val tabList: StateFlow<List<CnrTopLevelDestination>> =
        getTabs()
            .map { it ->
                it.map { tab ->
                    CnrTopLevelDestination(
                        route = tab.quoteAssetName,
                        selectedIconId = tab.icon,
                        unselectedIconId = tab.icon,
                        iconTextId = tab.icon
                    )
                }
            }.stateIn(
                coroutineScope,
                SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

    private fun getTabs() : Flow<Iterable<TabItem>> = flow {
        if (!tabInfoStorer.isInitialised) tabInfoStorer.initTabs()
        emit(tabInfoStorer.items)
    }

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: CnrTopLevelDestination) {
        trace("Navigation: ${topLevelDestination.route}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

//            when (topLevelDestination) {
//                FOR_YOU -> navController.navigateToForYou(topLevelNavOptions)
//                BOOKMARKS -> navController.navigateToBookmarks(topLevelNavOptions)
//                INTERESTS -> navController.navigateToInterestsGraph(topLevelNavOptions)
//            }
        }
    }
}