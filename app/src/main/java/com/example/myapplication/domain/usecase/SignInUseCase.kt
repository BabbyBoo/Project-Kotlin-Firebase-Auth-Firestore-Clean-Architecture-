package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = repo.signIn(email, password)
}