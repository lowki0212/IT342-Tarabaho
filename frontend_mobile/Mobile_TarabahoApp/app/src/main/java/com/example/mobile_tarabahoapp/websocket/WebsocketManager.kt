package com.example.mobile_tarabahoapp.websocket

import android.util.Log
import kotlinx.coroutines.*
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompHeader
import kotlin.coroutines.CoroutineContext

class WebsocketManager(
    private val bookingId: Long,
    private val token: String,
    private val onMessageReceived: (String) -> Unit
) : CoroutineScope {

    private var stompClient: StompClient? = null
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun connect() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "https://tarabaho-backend.onrender.com/chat")
        val headers = listOf(StompHeader("Authorization", "Bearer $token"))
        stompClient?.connect(headers)

        stompClient?.lifecycle()?.subscribe({ lifecycleEvent ->
            Log.d("Websocket", "Lifecycle: ${lifecycleEvent.type}")
        }, { error ->
            Log.e("Websocket", "Lifecycle error: ${error.message}")
        })

        stompClient?.topic("/topic/booking/$bookingId")?.subscribe({ topicMessage ->
            Log.d("Websocket", "Received: ${topicMessage.payload}")
            onMessageReceived(topicMessage.payload)
        }, { error ->
            Log.e("Websocket", "Error receiving message: ${error.message}")
        })
    }

    fun sendMessage(message: String) {
        if (stompClient?.isConnected == true) {
            stompClient?.send("/app/chat/$bookingId", message)
                ?.subscribe({
                    Log.d("Websocket", "Message sent successfully")
                }, { error ->
                    Log.e("Websocket", "Failed to send message: ${error.message}")
                })
        } else {
            Log.w("Websocket", "Cannot send message. Not connected yet.")
        }
    }

    fun disconnect() {
        stompClient?.disconnect()
        Log.d("Websocket", "Disconnected requested.")
        job.cancel()
    }
}
