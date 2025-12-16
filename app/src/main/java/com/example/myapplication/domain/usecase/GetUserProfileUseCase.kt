package com.example.myapplication.domain.usecase

import com.example.myapplication.data.datasource.FirestoreDataSource
import com.example.myapplication.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val store: FirestoreDataSource
) {
    operator fun invoke(uid: String): Flow<UserProfile?> {
        return store.getUserProfile(uid)
    }
}