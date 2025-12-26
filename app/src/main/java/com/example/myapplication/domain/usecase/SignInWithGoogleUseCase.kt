package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(idToken: String) = repo.signInWithGoogle(idToken)
}
