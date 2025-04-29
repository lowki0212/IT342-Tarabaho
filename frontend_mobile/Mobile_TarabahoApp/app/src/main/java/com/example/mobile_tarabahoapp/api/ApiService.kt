package com.example.mobile_tarabahoapp.api

import com.example.mobile_tarabahoapp.model.AuthResponse
import com.example.mobile_tarabahoapp.model.LoginRequest
import com.example.mobile_tarabahoapp.model.ProfileUpdateRequest
import com.example.mobile_tarabahoapp.model.RegisterRequest
import com.example.mobile_tarabahoapp.model.User
import com.example.mobile_tarabahoapp.model.Worker
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


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

    @POST("api/worker/token")
    suspend fun loginWorker(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @GET("api/worker/all")
    suspend fun getAllWorkers(): Response<List<Worker>>

    @GET("api/worker/category/{categoryName}/workers")
    suspend fun getWorkersByCategory(@Path("categoryName") categoryName: String): Response<List<Worker>>


}