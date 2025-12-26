package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.UserProfile
sealed class AuthResult {
    data class Success(val uid: String): AuthResult()
    data class Failure(val exception: Exception): AuthResult()
}

interface AuthRepository {
    suspend fun signUp(email: String, password: String, displayName: String?): AuthResult
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun saveUserProfile(profile: UserProfile): Result<Unit>
    suspend fun signInWithGoogle(idToken: String): AuthResult
    fun currentUserUid(): String?
}
