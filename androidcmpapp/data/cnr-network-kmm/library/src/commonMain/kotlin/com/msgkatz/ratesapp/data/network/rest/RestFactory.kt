package com.msgkatz.ratesapp.data.network.rest

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
fun getRestClient(): HttpClient = HttpClient {
    install(HttpTimeout) {
        socketTimeoutMillis = 60_000
        requestTimeoutMillis = 60_000
    }
    install(Logging) {
        //logger = Logger.DEFAULT
        //level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                println("KtorClient: ${message}")
//                    co.touchlab.kermit.Logger.d(tag = "KtorClient", null) {
//                        message
//                    }
            }
        }
    }
    defaultRequest {
        //url(BuildKonfig.BASE_URL)
        contentType(ContentType.Application.GZip)
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }
//    install(WebSockets) {
//        contentConverter = KotlinxWebsocketSerializationConverter(Json)
//    }
}

//@OptIn(ExperimentalSerializationApi::class)
//expect fun getRestClientPlatformed(): HttpClient