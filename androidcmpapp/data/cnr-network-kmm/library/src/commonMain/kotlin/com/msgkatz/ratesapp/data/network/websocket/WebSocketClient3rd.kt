package com.msgkatz.ratesapp.data.network.websocket

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*

class WebSocketClient3rd constructor( // Use "out T" for covariance
    private val coroutineScope: CoroutineScope,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default, // Changed to Dispatchers.Default
    private val debug: Boolean = false,
) {

    private val _messageFlow = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val messageFlow: SharedFlow<String> = _messageFlow.asSharedFlow() // Good practice for immutability

    private var curJob: Job? = null
    private var curClient: HttpClient? = null
    private val reconnectDelay: Long = 5000L // 5 seconds, adjust as needed
    //private val isConnected = AtomicBoolean(false) // Use AtomicBoolean for thread safety

    fun cancel() {
        curJob?.cancel() // Cancel the coroutine
        curJob = null  //Clean Job
        curClient?.close() // Close the WebSocket connection
        curClient = null //Clean HttpClient
    }

    // Unified connect function
    fun connect(path: String): SharedFlow<String> {
        cancel()

        val url = Url(path)
        curClient = getClient().also { client -> // Renamed to 'client' for clarity
            curJob = coroutineScope.launch(coroutineDispatcher) {
                while (true) {
                    try {
                        innerConnect(client, url)
                    } catch (e: Exception) {
                        // Log the exception (consider a more robust logging mechanism)
                        println("WebSocket connection error: ${e.message}")
                        //isConnected.set(false) // Set to false on any error
                        addStatus("error", e.message ?: "Unknown error") // Handle null messages
                    }

                    if(!coroutineScope.isActive) break; //break loop, if scope became inactive
                    println("Reconnecting in ${reconnectDelay}ms...")
                    delay(reconnectDelay) // Wait before reconnecting
                }
            }
        }

        return messageFlow
    }

    private suspend fun innerConnect(client: HttpClient, url: Url) {
        client.wss(method = HttpMethod.Get, host = url.host, port = url.port, path = url.encodedPathAndQuery) {
            try {
                println("WebSocket connected to $url")
                //isConnected.set(true)
                for (frame in incoming) {
                //incoming.consumeEach { frame -> // Changed variable name to 'frame'
                    ensureActive()
                    when (frame) {
                        is Frame.Text -> {
                            val msg = frame.readText()
                            //sendMessage(msg)
                            sendMessageSync(msg)
                            addStatus("msg", msg)
                        }
                        is Frame.Close -> {
                            addStatus("closed", closeReason.await()!!.message)
                            break
                        }
                        else -> {} // Handle other frame types if necessary
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
                println("WebSocket closed: ${closeReason.await()}")
            } catch (e: Throwable) {
                println("Error in WebSocket: ${e.message}")
                throw e // Re-throw to be caught in outer try-catch
            } finally {
                //isConnected.set(false)
                println("WebSocket connection closed.")
            }

        }
    }

    fun isConnected(): Boolean {
        //return isConnected.get()
        return false
    }


    private fun sendMessage(msg: String) {
        _messageFlow.tryEmit(msg)
    }

    private suspend fun sendMessageSync(msg: String) {
        _messageFlow.emit(msg)
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