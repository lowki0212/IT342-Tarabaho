package com.example.mobile_tarabahoapp.model

data class User(
    val id: Long,
    val firstname: String,
    val lastname: String,
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String?,
    val location: String?,
    val birthday: String,
    val profilePicture: String?
)
