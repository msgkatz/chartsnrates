package com.msgkatz.ratesapp

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.msgkatz.ratesapp.data.network.rest.PlatformInfoDTApiModel
import com.msgkatz.ratesapp.data.network.rest.PriceSimpleDTApiModel

actual suspend fun getLocalPlatformInfo(): Result<PlatformInfoDTApiModel> {
    try {
        val repo = DataRepository()
        val data = repo.loadPlatformInfo()
        repo.closeClient()
        return data
    } catch (e: Exception) {
        return Result.failure(e)
    }
}

actual suspend fun getLocalPriceSimple(): Result<List<PriceSimpleDTApiModel>> {
    try {
        val repo = DataRepository()
        val data = repo.loadPriceSimple()
        repo.closeClient()
        return data
    } catch (e: Exception) {
        return Result.failure(e)
    }
}



class DataRepository {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Optional: useful if JSON has more fields than your data class
                isLenient = true // Optional: allows some flexibility in JSON format
            })
        }
        // Optional: configure default host/port if fetching from a different origin
        // defaultRequest {
        //     url.protocol = URLProtocol.HTTP
        //     url.host = "localhost"
        //     url.port = 8080 // or your development server port
        // }
    }

    suspend fun loadPlatformInfo(): Result<PlatformInfoDTApiModel> {
        return runCatching {
            // Use the path relative to your web server's root
            val urlPath = "platform_info.json" // Adjust if your file is in a subdirectory within resources

            httpClient.get(urlPath).body<PlatformInfoDTApiModel>()
        }
    }

    suspend fun loadPriceSimple(): Result<List<PriceSimpleDTApiModel>> {
        return runCatching {
            // Use the path relative to your web server's root
            val urlPath = "price_simple.json" // Adjust if your file is in a subdirectory within resources

            httpClient.get(urlPath).body<List<PriceSimpleDTApiModel>>()
        }
    }

    // Don't forget to close the client when done,
    // or manage its lifecycle appropriately
    fun closeClient() {
        httpClient.close()
    }
}