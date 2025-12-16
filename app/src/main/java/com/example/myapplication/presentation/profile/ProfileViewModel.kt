package com.example.myapplication.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.UserProfile
import com.example.myapplication.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(val profile: UserProfile?) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf<ProfileUiState>(ProfileUiState.Loading)
    val uiState: State<ProfileUiState> = _uiState

    private var loadJob: Job? = null

    fun loadProfile(uid: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            getUserProfileUseCase(uid)
                .catch { e ->
                    _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error")
                }
                .collect { profile ->
                    _uiState.value = ProfileUiState.Success(profile)
                }
        }
    }
}
