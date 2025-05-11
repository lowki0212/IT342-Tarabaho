package com.example.mobile_tarabahoapp.model

data class Message(
    val id: Long,
    val bookingId: Long,
    val senderUserId: Long?,
    val senderWorkerId: Long?,
    val senderName: String,
    val content: String,
    val sentAt: String // ISO-8601 string, parse if needed
)
