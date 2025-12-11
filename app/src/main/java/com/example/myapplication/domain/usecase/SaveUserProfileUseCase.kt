package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.UserProfile
import com.example.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class SaveUserProfileUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(profile: UserProfile) = repo.saveUserProfile(profile)
}