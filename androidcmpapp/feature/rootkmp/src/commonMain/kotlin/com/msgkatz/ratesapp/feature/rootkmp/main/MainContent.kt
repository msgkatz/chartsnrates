package com.msgkatz.ratesapp.feature.rootkmp.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.msgkatz.ratesapp.data.model.PriceSimple
import com.msgkatz.ratesapp.feature.rootkmp.rootkmp.generated.resources.Res
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainContent(component: MainIFace) {
    ScreenMain(
        component = component,
        onPriceItemClick = {},
        topLevelDestination = listOf(
            CnrTopLevelDestination.QAUSDT,
            CnrTopLevelDestination.QABNB,
            CnrTopLevelDestination.QABTC,
            CnrTopLevelDestination.QAETH
        )
    )
}

@Composable
fun ScreenMain(
    modifier: Modifier = Modifier,
    //appState: CnrAppState,
    component: MainIFace,
    onPriceItemClick: (PriceSimple) -> Unit,
    topLevelDestination: List<CnrTopLevelDestination>
) {
    val isBottomBarVisible = false //windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    val isNavigationRailVisible = !isBottomBarVisible
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        //backgroundColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            if (isBottomBarVisible) {
                CnrNavBar(
                    destinations = topLevelDestination,
                    //onNavigateToDestination = appState::navigateToTopLevelDestination,
                    //currentDestination = appState.currentDestination,
                    //appState = appState,
                    component = component,
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
            if (isNavigationRailVisible) {
            }

            Column(Modifier.fillMaxSize()) {
                Children(
                    component = component,
                    onPriceItemClick = onPriceItemClick
                )
            }
        }

    }
}

@Composable
fun Children(
    component: MainIFace,
    onPriceItemClick: (PriceSimple) -> Unit,
) {
    Children(
        stack = component.childStack,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            fallbackAnimation = stackAnimation(fade()),
            onBack = component::navigateBack
        ),
    ) {
        when (val child = it.instance) {
            is MainIFace.Child.QAUsdt -> QuoteAssetContent(child.component)
            is MainIFace.Child.QABnb -> QuoteAssetContent(child.component)
            is MainIFace.Child.QABtc -> QuoteAssetContent(child.component)
            is MainIFace.Child.QAEth -> QuoteAssetContent(child.component)
        }
    }
}

@Composable
fun CnrNavBar(
    destinations: List<CnrTopLevelDestination>,
    //onNavigateToDestination: (CnrTopLevelDestination) -> Unit,
    component: MainIFace,
    modifier: Modifier = Modifier,
) {
    val stack by component.childStack.subscribeAsState()
    val child by remember { derivedStateOf { stack.active.instance } }

    CnrNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            CnrNavigationBarItem(
                selected = destination == child.destination,
                onClick = {
                    component.navigateTo(destination)
                },
                icon = {
                    Icon(
                        painter = painterResource(resource = destination.selectedIcon),
                        contentDescription = null,
                    )
                },
            )

        }
    }

}

private val MainIFace.Child.destination: CnrTopLevelDestination
    get() =
        when (this) {
            is MainIFace.Child.QAUsdt -> CnrTopLevelDestination.QAUSDT
            is MainIFace.Child.QABnb -> CnrTopLevelDestination.QABNB
            is MainIFace.Child.QABtc -> CnrTopLevelDestination.QABTC
            is MainIFace.Child.QAEth -> CnrTopLevelDestination.QAETH
        }

private fun MainIFace.navigateTo(destination: CnrTopLevelDestination) {
    when (destination) {
        CnrTopLevelDestination.QAUSDT -> navigateToUsdt()
        CnrTopLevelDestination.QABNB -> navigateToBnb()
        CnrTopLevelDestination.QABTC -> navigateToBtc()
        CnrTopLevelDestination.QAETH -> navigateToEth()
    }
}


//data class CnrTopLevelDestination(
//    val route: String,
//    val selectedIconId: Int,
//    val unselectedIconId: Int,
//    val iconTextId: Int
//)

enum class CnrTopLevelDestination(
    val route: String,
    val selectedIcon: DrawableResource,
    val selectedIconId: Int,
    val unselectedIconId: Int,
    //val iconTextId: Int
) {
    QAUSDT(
        route = "USDT",
        selectedIcon = Res.drawable.cur_usdt,
        selectedIconId = Res.drawable.cur_usdt,
        unselectedIconId = Res.drawable.cur_usdt,
        //iconTextId = R.string.qa_usdt
    ),
    QABNB(
        route = "BNB",
        selectedIcon = Res.drawable.cur_bnb,
        selectedIconId = Res.drawable.cur_bnb,
        unselectedIconId = Res.drawable.cur_bnb,
        //iconTextId = R.string.qa_usdt
    ),
    QABTC(
        route = "BTC",
        selectedIcon = Res.drawable.cur_btc,
        selectedIconId = Res.drawable.cur_btc,
        unselectedIconId = Res.drawable.cur_btc,
        //iconTextId = R.string.qa_usdt
    ),
    QAETH(
    route = "ETH",
    selectedIcon = Res.drawable.cur_eth,
    selectedIconId = Res.drawable.cur_eth,
    unselectedIconId = Res.drawable.cur_eth,
    //iconTextId = R.string.qa_usdt
    )
}
