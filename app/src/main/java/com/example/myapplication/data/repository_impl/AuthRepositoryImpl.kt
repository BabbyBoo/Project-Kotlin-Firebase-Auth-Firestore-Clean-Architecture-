package com.example.myapplication.data.repository_impl

import com.example.myapplication.data.datasource.FirebaseAuthDataSource
import com.example.myapplication.data.datasource.FirestoreDataSource
import com.example.myapplication.domain.model.UserProfile
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.AuthResult
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDs: FirebaseAuthDataSource,
    private val storeDs: FirestoreDataSource
) : AuthRepository {

    override suspend fun signUp(email: String, password: String, displayName: String?): AuthResult {
        val result = authDs.createUser(email, password)

        return result.fold(
            onSuccess = { uid ->
                try {
                    val profile = UserProfile(
                        uid = uid,
                        email = email,
                        displayName = displayName
                    )
                    storeDs.saveUserProfile(profile)
                    AuthResult.Success(uid)
                } catch (e: Exception) {
                    AuthResult.Failure(e)
                }
            },
            onFailure = { e -> AuthResult.Failure(e as Exception) }
        )
    }

    override suspend fun signIn(email: String, password: String): AuthResult {
        val result = authDs.signIn(email, password)
        return result.fold(
            onSuccess = { uid -> AuthResult.Success(uid) },
            onFailure = { e -> AuthResult.Failure(e as Exception) }
        )
    }

    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            storeDs.saveUserProfile(profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun currentUserUid(): String? = authDs.currentUid()
}
