package com.msgkatz.ratesapp.feature.rootkmp.main

import com.arkivanov.decompose.ComponentContext

class QuoteAssetComponent internal constructor(
    componentContext: ComponentContext
): QuoteAssetIFace, ComponentContext by componentContext {
}