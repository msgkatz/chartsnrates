package com.msgkatz.ratesapp.feature.quoteassetkmp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

class MainComponent internal constructor(
    componentContext: ComponentContext
): MainIFace {
    override val childStack: Value<ChildStack<*, MainIFace.Child>>
        get() = TODO("Not yet implemented")
}