package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUidUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String? = authRepository.currentUserUid()
}