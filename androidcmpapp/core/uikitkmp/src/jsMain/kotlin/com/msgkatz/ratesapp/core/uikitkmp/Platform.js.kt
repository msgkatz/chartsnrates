package com.msgkatz.ratesapp.core.uikitkmp

class JSPlatform: Platform {
    override val name: String = "Js platform "
}

actual fun getPlatform(): Platform = JSPlatform()