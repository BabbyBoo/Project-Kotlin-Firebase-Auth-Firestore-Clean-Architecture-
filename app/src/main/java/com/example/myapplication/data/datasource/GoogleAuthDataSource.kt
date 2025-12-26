package com.example.myapplication.data.datasource

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.example.myapplication.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleAuthDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth // Sửa lỗi: Inject FirebaseAuth thay vì GoogleAuthProvider
) {

    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken("950039281032-3e70pvvcrco34f3toe3tadaaqo5419pi.apps.googleusercontent.com")
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }
    
    suspend fun signInWithGoogle(idToken: String): Result<String> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            // Sửa lỗi: Gọi `signInWithCredential` trên instance của FirebaseAuth
            val result = auth.signInWithCredential(credential).await()
            val uid = result.user?.uid ?: ""
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
