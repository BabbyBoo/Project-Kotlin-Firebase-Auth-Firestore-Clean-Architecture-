package com.example.myapplication.domain.model

data class UserProfile(
    val uid: String,
    val email: String,
    val displayName: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)