package com.example.myapplication.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication.domain.model.UserProfile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val userCollection = firestore.collection("users")

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            userCollection.document(profile.uid)
                .set(profile)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserProfile(uid: String): Flow<UserProfile?> = callbackFlow {
        val subscription = userCollection.document(uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                try {
                    val profile = snapshot.toObject(UserProfile::class.java)
                    trySend(profile)
                } catch (e: Exception) {
                    close(e)
                }
            } else {
                trySend(null)
            }
        }
        awaitClose { subscription.remove() }
    }
}
