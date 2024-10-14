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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * BNBBTC
 *
 * "wss://stream.binance.com:9443";
 * /ws/
 * BNBBTC@miniTicker
 *
 * wss://stream.binance.com:9443/ws/BNBBTC@miniTicker
 *
 * val url = "wss://stream.binance.com:9443/ws/BNBBTC@miniTicker"
 * val url2 = "wss://stream.binance.com:9443/ws/!ticker@arr"
 */
class WebSocketClient constructor(
    private val coroutineScope: CoroutineScope,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Unconfined,
) {

    private val _messageFlow = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val messageFlow = _messageFlow


    private var curJob: Job? = null
    private var curClient: HttpClient? = null

    private fun cancel() {
        curJob?.cancel()
        curJob = null
        curClient?.close()
        curClient = null
    }

    fun connect(path: String): SharedFlow<String> {
        cancel()

        val url = Url(path)
        curClient = getClient().also {
            curJob = coroutineScope.launch(coroutineDispatcher) {
                try {
                    innerConnect(it, url)
                } catch (e: Throwable) {
                    addStatus("error", e.message!!)
                    //this.cancel()
                }
            }
        }

        return messageFlow
    }

    private suspend fun innerConnect(client: HttpClient, url : Url) {
        client.wss(method = HttpMethod.Get, host = url.host, port = url.port, path = url.encodedPathAndQuery) {
                async {
                    incoming.consumeEach {
                        when (it) {
                            is Frame.Text -> {
                                val msg = it.readText()
                                sendMessage(msg)
                                addStatus("msg", msg)

                            }

                            is Frame.Close ->
                                addStatus("closed", closeReason.await()!!.message)

                            else -> {}
                        }
                    }
                }.await()

        }
    }

    private fun sendMessage(msg: String) {
        _messageFlow.tryEmit(msg)
    }

    private fun addStatus(state: String, arg: String) {
        if (!DEBUG) return
        println("$state:$arg")
    }

    companion object {
        private val DEBUG = true
    }
}