package com.example.mobile_tarabahoapp.api.chat

import com.example.mobile_tarabahoapp.api.ApiService
import com.example.mobile_tarabahoapp.api.RetrofitClient.apiService
import com.example.mobile_tarabahoapp.model.Message
import com.example.mobile_tarabahoapp.model.SendMessageRequest
import retrofit2.Response

class ChatRepository(private val api: ApiService) {

    suspend fun getMessages(bookingId: Long): List<Message> {
        return try {
            val response = api.getMessages(bookingId) // ✅ FIXED from apiService → api
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


    suspend fun sendMessage(request: SendMessageRequest): Response<Message> {
        return api.sendMessage(request)
    }
}
