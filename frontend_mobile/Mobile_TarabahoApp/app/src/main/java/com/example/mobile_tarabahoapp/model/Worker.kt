package com.example.mobile_tarabahoapp.model

data class Worker(
    val id: Long? = null,
    val username: String = "",
    val password: String = "",
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val biography: String? = null,
    val birthday: String? = null,
    val profilePicture: String? = null,
    val hourly: Double? = null,
    val isAvailable: Boolean? = null,
    val isVerified: Boolean? = null,
    val stars: Double? = null,
    val ratingCount: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
