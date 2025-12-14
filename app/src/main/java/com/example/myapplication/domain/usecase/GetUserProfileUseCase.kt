package com.example.myapplication.domain.usecase

import com.example.myapplication.data.datasource.FirestoreDataSource
import com.example.myapplication.domain.model.UserProfile
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val store: FirestoreDataSource
) {
    suspend operator fun invoke(uid: String): Result<UserProfile?> {
        return store.getUserProfile(uid)
    }
}