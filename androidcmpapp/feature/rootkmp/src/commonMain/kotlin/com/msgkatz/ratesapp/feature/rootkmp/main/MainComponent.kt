package com.msgkatz.ratesapp.feature.rootkmp.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

//import com.msgkatz.ratesapp.feature.quoteassetkmp.MainIFace

class MainComponent //internal
constructor(
    componentContext: ComponentContext,
    private val quoteAsset: (ComponentContext, String) -> QuoteAssetIFace
): MainIFace, ComponentContext by componentContext  {

    private val navigation = StackNavigation<Configuration>()

    private val stack =
        childStack(
            source = navigation,
            initialConfiguration = Configuration.QAUSDT,
            handleBackButton = true,
            childFactory = ::createChild,
            serializer = Configuration.serializer()
        )

    override val childStack: Value<ChildStack<*, MainIFace.Child>>
        get() = stack

    override fun navigateBack() {
        navigation.pop()
    }

    override fun navigateToUsdt() {
        navigation.bringToFront(Configuration.QAUSDT)
    }

    override fun navigateToBnb() {
        navigation.bringToFront(Configuration.QABNB)
    }

    override fun navigateToBtc() {
        navigation.bringToFront(Configuration.QABTC)
    }

    override fun navigateToEth() {
        navigation.bringToFront(Configuration.QAETH)
    }

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): MainIFace.Child =
        when (configuration) {
            is Configuration.QAUSDT -> MainIFace.Child.QAUsdt(quoteAsset(componentContext, "USDT"))
            is Configuration.QABNB -> MainIFace.Child.QAUsdt(quoteAsset(componentContext, "BNB"))
            is Configuration.QABTC -> MainIFace.Child.QABtc(quoteAsset(componentContext, "BTC"))
            is Configuration.QAETH -> MainIFace.Child.QABtc(quoteAsset(componentContext, "ETH"))
        }

    @Serializable
    private sealed class Configuration {
        @Serializable
        data object QAUSDT : Configuration()

        @Serializable
        data object QABNB : Configuration()

        @Serializable
        data object QABTC : Configuration()

        @Serializable
        data object QAETH : Configuration()
    }
}

