package com.example.myapplication.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication.domain.model.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val userCollection = firestore.collection("users")

    suspend fun saveUserProfile(profile: UserProfile) {
        userCollection.document(profile.uid)
            .set(profile)
            .await()
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        val doc = userCollection.document(uid).get().await()
        return if (doc.exists()) doc.toObject(UserProfile::class.java) else null
    }
}
