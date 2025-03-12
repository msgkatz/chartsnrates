package com.msgkatz.ratesapp.data.network.websocket

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class WebSocketClientTyped<T> constructor(
    private val coroutineScope: CoroutineScope,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
    private val debug: Boolean = false,
) {

    private val _messageFlow: MutableSharedFlow<T> = MutableSharedFlow<T>(replay = 0, extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val messageFlow: SharedFlow<T> = _messageFlow

    private var curJob: Job? = null
    private var curClient: HttpClient? = null

    fun cancel() {
        curJob?.cancel()
        curJob = null
        curClient?.close()
        curClient = null
    }

    fun connectTyped(path: String, converter: (String) -> T?): SharedFlow<T> {
        cancel()

        val url = Url(path)

        curClient = getClient().also {
            curJob = coroutineScope.launch(coroutineDispatcher) {
                try {
                    innerConnectTyped(it, url, converter)
                } catch (e: Throwable) {
                    addStatus("error", e.message!!)
                    //this.cancel()
                }
            }
        }

        return messageFlow
    }

    //TODO: refactor like in
    //TODO: https://github.com/ktorio/ktor-documentation/blob/3.1.0/codeSnippets/snippets/client-websockets/src/main/kotlin/com/example/Application.kt
    //TODO: prolly no need in async - can use while(true)
    //TODO: or just use launch
    private suspend fun innerConnectTyped(client: HttpClient, url : Url, converter: (String) -> T?) {
        client.wss(method = HttpMethod.Get, host = url.host, port = url.port, path = url.encodedPathAndQuery) {
            async {
                incoming.consumeEach {
                    when (it) {
                        is Frame.Text -> {
                            val msg = it.readText()
                            converter(msg)?.let{
                                sendMessage(it)
                            }
                            addStatus("msg", msg)
                        }

                        //TODO: there's no control frames in incoming
                        is Frame.Close ->
                            addStatus("closed", closeReason.await()!!.message)

                        else -> {}
                    }
                }
            }.await()

        }
    }



    private fun sendMessage(msg: T?) {
        msg?.let { _messageFlow.tryEmit(it) }
    }

    private fun addStatus(state: String, arg: String) {
        if (!debug) return
        println("$state:$arg")
    }

    companion object {
        @Deprecated("changed to constructor param")
        private val DEBUG = true
    }
}