package com.msgkatz.ratesapp.data.network.rest

import io.ktor.client.HttpClient
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(markerClass = [ExperimentalSerializationApi::class])
actual fun getRestClientPlatformed(): HttpClient {
    TODO("Not yet implemented")
}