package com.msgkatz.ratesapp.core.uikitkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform