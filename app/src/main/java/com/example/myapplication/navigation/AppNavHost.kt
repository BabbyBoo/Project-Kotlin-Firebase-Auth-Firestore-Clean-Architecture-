package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType

import com.example.myapplication.presentation.auth.SignInScreen
import com.example.myapplication.presentation.auth.SignUpScreen
import com.example.myapplication.presentation.profile.ProfileScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavRoutes.SIGN_IN
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // -------- SIGN IN ----------
        composable(NavRoutes.SIGN_IN) {
            SignInScreen(
                onSignedIn = { uid ->
                    if (uid.isNotBlank()) {
                        navController.navigate("${NavRoutes.PROFILE}/$uid") {
                             popUpTo(NavRoutes.SIGN_IN) { inclusive = true }
                        }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(NavRoutes.SIGN_UP)
                }
            )
        }

        // -------- SIGN UP ----------
        composable(NavRoutes.SIGN_UP) {
            SignUpScreen(
                onSignedUp = { uid ->
                     navController.navigate(NavRoutes.SIGN_IN) {
                         popUpTo(NavRoutes.SIGN_UP) { inclusive = true }
                     }
                }
            )
        }

        // -------- PROFILE ----------
        composable(
            route = NavRoutes.PROFILE_WITH_UID,
            arguments = listOf(
                navArgument(NavRoutes.ARG_UID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val uid = backStackEntry.arguments?.getString(NavRoutes.ARG_UID) ?: ""
            ProfileScreen(uid = uid)
        }
    }
}
