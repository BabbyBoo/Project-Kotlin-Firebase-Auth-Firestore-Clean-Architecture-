package com.example.myapplication.domain.usecase

import com.example.myapplication.data.repository_impl.AuthRepositoryImpl
import javax.inject.Inject

class GetGoogleSignInClientUseCase @Inject constructor(
    private val repo: AuthRepositoryImpl
) {
    operator fun invoke() = repo.getGoogleClient()
}
