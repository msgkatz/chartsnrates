package com.msgkatz.ratesapp.data.network.rest

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpStatement
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.core.use
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resumeWithException


//suspend fun Call.await() = suspendCancellableCoroutine<ResponseBody?> { continuation ->
//    continuation.invokeOnCancellation {
//        cancel()
//    }
//    enqueue(object : Callback {
//        override fun onResponse(call: Call, response: Response) {
//            continuation.resume(response.body)
//        }
//        override fun onFailure(call: Call, e: IOException) {
//            continuation.resumeWithException(e)
//        } })
//}

//class ResponseBody(data: Any)
suspend fun HttpClientCall.await() = suspendCancellableCoroutine<HttpResponse> {
        continuation ->


        val job = launch {

            try {
                val body = body(typeInfo<HttpResponse>()) as HttpResponse
                if (!continuation.isCancelled)
                    continuation.resume(body) {}
            } catch (ex: Exception) {
                continuation.resumeWithException(ex)
            }
        }

    continuation.invokeOnCancellation {
        job.cancel()
        cancel(CancellationException("stopped"))
    }

}


//suspend fun HttpStatement.await() = suspendCancellableCoroutine<HttpResponse?> {
//    continuation ->
//
//
//    val job = launch {
//        execute { response ->
//            try {
//
//                continuation.resume(response) {}
//            } catch (ex: Exception) {
//                continuation.resumeWithException(ex)
//            }
//        }
//
//    }
//
//    continuation.invokeOnCancellation {
//        job.cancel()
//    }
//
//}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun HttpClient.await() = suspendCancellableCoroutine<HttpResponse?> {
        continuation ->
    continuation.invokeOnCancellation {

        close()
        //cancel(CancellationException("stopped"))

    }


    launch {
        try {
            val body: HttpResponse? = get("", {})
            continuation.resume(body) {}
        } catch (ex: Exception) {
            continuation.resumeWithException(ex)
        }
    }

}