package com.example.myapplication.presentation.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun SignInScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignedIn: (String) -> Unit,      // callback điều hướng sang Profile
    onNavigateToSignUp: () -> Unit     // callback điều hướng sang SignUp
) {
    val uiState = viewModel.uiState

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    val googleSignInClient = remember {
        viewModel.getGoogleSignInClient()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken!!
                viewModel.signInWithGoogle(idToken)
            } catch (e: ApiException) {
                // Xử lý lỗi
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("Sign In", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { viewModel.signIn(email, pass) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Spacer(Modifier.height(16.dp))
        
        TextButton(
            onClick = onNavigateToSignUp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Don't have an account? Sign Up")
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            launcher.launch(googleSignInClient.signInIntent)
        }) {
            Text("Login with Google")
        }

        Spacer(Modifier.height(16.dp))

        when (uiState) {
            is AuthUiState.Loading -> CircularProgressIndicator()
            is AuthUiState.Error -> Text("Error: ${uiState.message}", color = MaterialTheme.colorScheme.error)
            is AuthUiState.Success -> {
                LaunchedEffect(uiState.uid) {
                    onSignedIn(uiState.uid)
                }
            }
            else -> {}
        }
    }
}