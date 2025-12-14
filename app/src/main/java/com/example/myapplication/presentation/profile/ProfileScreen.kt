package com.example.myapplication.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    uid: String,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    require(uid.isNotEmpty())

    val uiState = viewModel.uiState.value

    LaunchedEffect(uid) {
        viewModel.loadProfile(uid)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("User Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        when (uiState) {
            is ProfileUiState.Loading -> {
                CircularProgressIndicator()
            }
            is ProfileUiState.Error -> {
                Text(
                    text = "Error: ${uiState.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            is ProfileUiState.Success -> {
                val profile = uiState.profile
                if (profile != null) {
                    Text("UID: ${profile.uid}")
                    Spacer(Modifier.height(12.dp))

                    Text("Email: ${profile.email}")
                    Spacer(Modifier.height(12.dp))

                    Text("DisplayName: ${profile.displayName ?: "No name"}")
                    Spacer(Modifier.height(12.dp))

                    Text("Created At: ${profile.createdAt}")
                } else {
                    Text("Profile not found")
                }
            }
        }
    }
}
