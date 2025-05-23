package com.example.mobile_tarabahoapp.api

import android.os.Message
import com.example.mobile_tarabahoapp.model.AuthResponse
import com.example.mobile_tarabahoapp.model.Booking
import com.example.mobile_tarabahoapp.model.CategoryBookingRequest
import com.example.mobile_tarabahoapp.model.LoginRequest
import com.example.mobile_tarabahoapp.model.ProfileUpdateRequest
import com.example.mobile_tarabahoapp.model.RatingRequest
import com.example.mobile_tarabahoapp.model.RegisterRequest
import com.example.mobile_tarabahoapp.model.SendMessageRequest
import com.example.mobile_tarabahoapp.model.User
import com.example.mobile_tarabahoapp.model.Worker
import com.example.mobile_tarabahoapp.model.WorkerRegisterRequest
import com.example.mobile_tarabahoapp.model.WorkerUpdateRequest
import com.example.mobile_tarabahoapp.model.MessageDTO
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

    @GET("api/worker/{id}")
    suspend fun getWorkerById(@Path("id") id: String): Response<Worker>

    @PUT("api/worker/{id}")
    suspend fun updateWorkerProfile(@Path("id") id: Long, @Body request: WorkerUpdateRequest): Response<Worker>

    @GET("/api/worker/username/{username}")
    suspend fun getWorkerByUsername(@Path("username") username: String): Response<Worker>

    @POST("api/worker/login")
    suspend fun sessionLoginWorker(@Body worker: Worker): Response<Worker>

    @GET("api/booking/worker")
    suspend fun getWorkerBookings(): Response<List<Booking>>

    @POST("/api/booking/{bookingId}/accept")
    suspend fun acceptBooking(@Path("bookingId") bookingId: Long): Response<Booking>

    @POST("/api/booking/{bookingId}/reject")
    suspend fun rejectBooking(@Path("bookingId") bookingId: Long): Response<Booking>

    @POST("/api/booking/{bookingId}/complete")
    suspend fun completeBooking(@Path("bookingId") bookingId: Long): Response<String>

    @POST("/api/booking/{bookingId}/cancel")
    suspend fun cancelBooking(@Path("bookingId") bookingId: Long): Response<String>

    @POST("/api/booking/{bookingId}/start")
    suspend fun startBooking(@Path("bookingId") bookingId: Long): Response<Void>

    @POST("/api/booking/{bookingId}/complete/accept")
    suspend fun acceptCompletion(@Path("bookingId") bookingId: Long): Response<String>

    @POST("/api/booking/category")
    suspend fun createCategoryBooking(@Body request: CategoryBookingRequest): Response<Booking>

    @GET("api/booking/{bookingId}/status")
    suspend fun getBookingStatus(@Path("bookingId") bookingId: Long): Response<BookingStatusResponse>
    data class BookingStatusResponse(val status: String)

    @GET("api/booking/{bookingId}")
    suspend fun getBookingById(@Path("bookingId") bookingId: Long): Response<Booking>

    @POST("/api/booking/{bookingId}/complete/accept")
    suspend fun acceptBookingCompletion(@Path("bookingId") bookingId: Long): Response<Void>

    @POST("/api/booking/rating")
    suspend fun submitRating(@Body request: RatingRequest): Response<Void>

    @GET("/api/message/booking/{bookingId}")
    suspend fun getMessages(@Path("bookingId") bookingId: Long): Response<List<MessageDTO>>

    @POST("/api/message/send")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<MessageDTO>

    @POST("/api/worker/register")
    suspend fun registerWorker(@Body request: WorkerRegisterRequest): Response<Worker>

    interface BookingApiService {
        @GET("/api/booking/user")
        suspend fun getUserBookings(): Response<List<Booking>>
    }

    @GET("/api/booking/user")
    suspend fun getUserBookings(): Response<List<Booking>>


}