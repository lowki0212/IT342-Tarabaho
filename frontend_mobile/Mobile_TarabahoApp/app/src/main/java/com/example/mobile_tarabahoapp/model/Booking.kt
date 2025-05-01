package com.example.mobile_tarabahoapp.model
import com.google.gson.annotations.SerializedName

data class Booking(
    val id: Long,
    val user: User?,              // nullable to handle missing data gracefully
    val worker: Worker?,          // included to reflect the backend (not in your version)
    val category: Category,
    val type: String,
    val status: String,
    val paymentMethod: String,
    val latitude: Double?,
    val longitude: Double?,
    val radius: Double?,
    val createdAt: String,
    val updatedAt: String?,
    val jobDetails: String
)