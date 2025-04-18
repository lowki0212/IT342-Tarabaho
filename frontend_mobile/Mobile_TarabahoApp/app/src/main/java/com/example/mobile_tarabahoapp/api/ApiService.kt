package com.example.mobile_tarabahoapp.api

import com.example.mobile_tarabahoapp.model.AuthResponse
import com.example.mobile_tarabahoapp.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("api/user/token")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>
}