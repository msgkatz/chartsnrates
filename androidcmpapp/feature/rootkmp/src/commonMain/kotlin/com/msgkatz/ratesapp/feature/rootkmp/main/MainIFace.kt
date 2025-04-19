package com.msgkatz.ratesapp.feature.rootkmp.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner

interface MainIFace: BackHandlerOwner {

    val childStack: Value<ChildStack<*, Child>>

    fun navigateBack()
    fun navigateToUsdt()
    fun navigateToBnb()
    fun navigateToBtc()
    fun navigateToEth()

    sealed class Child {
        data class QAUsdt(val component: QuoteAssetIFace) : Child()
        data class QABnb(val component: QuoteAssetIFace) : Child()
        data class QABtc(val component: QuoteAssetIFace) : Child()
        data class QAEth(val component: QuoteAssetIFace) : Child()
    }

}