package com.example.myapplication.data.repository_impl

import com.example.myapplication.data.datasource.FirebaseAuthDataSource
import com.example.myapplication.data.datasource.FirestoreDataSource
import com.example.myapplication.data.datasource.GoogleAuthDataSource
import com.example.myapplication.domain.model.UserProfile
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.AuthResult
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDs: FirebaseAuthDataSource,
    private val googleAuthDS: GoogleAuthDataSource,
    private val storeDs: FirestoreDataSource
) : AuthRepository {

    override suspend fun signUp(email: String, password: String, displayName: String?): AuthResult {
        val result = firebaseAuthDs.createUser(email, password)

        return result.fold(
            onSuccess = { uid ->
                val profile = UserProfile(
                    uid = uid,
                    email = email,
                    displayName = displayName
                )
                val saveResult = storeDs.saveUserProfile(profile)
                saveResult.fold(
                    onSuccess = { AuthResult.Success(uid) },
                    onFailure = { e -> AuthResult.Failure(e as Exception) }
                )
            },
            onFailure = { e -> AuthResult.Failure(e as Exception) }
        )
    }

    override suspend fun signIn(email: String, password: String): AuthResult {
        val result = firebaseAuthDs.signIn(email, password)
        return result.fold(
            onSuccess = { uid -> AuthResult.Success(uid) },
            onFailure = { e -> AuthResult.Failure(e as Exception) }
        )
    }

    fun getGoogleClient() = googleAuthDS.getGoogleSignInClient()

    override suspend fun signInWithGoogle(idToken: String): AuthResult {
        val result = googleAuthDS.signInWithGoogle(idToken)
        return result.fold(
            onSuccess = { uid -> AuthResult.Success(uid) },
            onFailure = { e -> AuthResult.Failure(e as Exception) }
        )
    }

    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return storeDs.saveUserProfile(profile)
    }

    override fun currentUserUid(): String? = firebaseAuthDs.currentUid()
}
