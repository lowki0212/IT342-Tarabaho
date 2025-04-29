package com.example.mobile_tarabahoapp.model

data class Worker(
    val id: Long,
    val username: String,
    val password: String?, // Optional depending on endpoint (can hide in UI)
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val address: String?,
    val biography: String?,
    val birthday: String?, // format: yyyy-MM-dd
    val profilePicture: String?, // Supabase URL
    val hourly: Double,
    val isAvailable: Boolean,
    val isVerified: Boolean,
    val stars: Double,
    val ratingCount: Int,
    val latitude: Double?,
    val longitude: Double?
)   