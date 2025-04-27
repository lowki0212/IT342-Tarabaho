package com.example.mobile_tarabahoapp.api
import com.example.mobile_tarabahoapp.utils.AuthInterceptor
import com.example.mobile_tarabahoapp.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // ✅ Use the custom AuthInterceptor class
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor()) // this is your actual interceptor class
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // applies AuthInterceptor to all requests
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}