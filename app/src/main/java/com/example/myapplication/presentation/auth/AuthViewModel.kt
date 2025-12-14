package com.example.myapplication.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.repository.AuthResult
import com.example.myapplication.domain.usecase.SignInUseCase
import com.example.myapplication.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthUiState {
    object Idle: AuthUiState
    object Loading: AuthUiState
    data class Success(val uid: String): AuthUiState
    data class Error(val message: String): AuthUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    fun signUp(email:String, pass:String, displayName:String?) {
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            when(val res = signUpUseCase(email, pass, displayName)) {
                is AuthResult.Success -> uiState = AuthUiState.Success(res.uid)
                is AuthResult.Failure -> uiState = AuthUiState.Error(res.exception.message ?: "Unknown")
            }
        }
    }

    fun signIn(email:String, pass:String) {
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            when(val res = signInUseCase(email, pass)) {
                is AuthResult.Success -> uiState = AuthUiState.Success(res.uid)
                is AuthResult.Failure -> uiState = AuthUiState.Error(res.exception.message ?: "Unknown")
            }
        }
    }
}
