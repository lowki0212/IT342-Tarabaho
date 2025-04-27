package com.example.mobile_tarabahoapp.api

import com.example.mobile_tarabahoapp.model.AuthResponse
import com.example.mobile_tarabahoapp.model.LoginRequest
import com.example.mobile_tarabahoapp.model.ProfileUpdateRequest
import com.example.mobile_tarabahoapp.model.RegisterRequest
import com.example.mobile_tarabahoapp.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


interface ApiService {
    @POST("api/user/token")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("api/user/register")
    suspend fun register(@Body req: RegisterRequest): Response<User>

    @POST("api/user/logout")
    suspend fun logout(): Response<String>

    @PUT("api/user/update-profile")
    suspend fun updateProfile(@Body request: ProfileUpdateRequest): Response<User>

    @GET("api/user/me")
    suspend fun getCurrentUser(): Response<User>

}