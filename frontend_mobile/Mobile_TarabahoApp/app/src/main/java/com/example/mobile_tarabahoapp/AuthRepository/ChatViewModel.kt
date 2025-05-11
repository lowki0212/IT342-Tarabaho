package com.example.mobile_tarabahoapp.AuthRepository

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_tarabahoapp.api.chat.ChatRepository
import com.example.mobile_tarabahoapp.model.Message
import com.example.mobile_tarabahoapp.model.SendMessageRequest
import com.example.mobile_tarabahoapp.websocket.WebsocketManager
import kotlinx.coroutines.launch

class ChatViewModel(
    private val bookingId: Long,
    private val token: String
) : ViewModel() {

    private val _messages = mutableStateOf<List<String>>(emptyList())
    val messages: State<List<String>> = _messages

    val newMessage = mutableStateOf("")

    private var websocketManager: WebsocketManager? = null

    init {
        connectWebSocket()
    }

    private fun connectWebSocket() {
        websocketManager = WebsocketManager(bookingId, token) { message ->
            // Append new message to UI
            _messages.value = _messages.value + message
        }
        websocketManager?.connect()
    }

    fun sendMessage() {
        val message = newMessage.value
        if (message.isNotBlank()) {
            websocketManager?.sendMessage(message)
            newMessage.value = ""
        }
    }

    fun onMessageChange(text: String) {
        newMessage.value = text
    }

    override fun onCleared() {
        websocketManager?.disconnect()
        super.onCleared()
    }
}

