package com.example.bloom_app.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bloom_app.ui.screen.auth.AuthScreen
import com.example.bloom_app.ui.screen.auth.AuthViewModel
import com.example.bloom_app.ui.screen.auth.AuthState
import com.example.bloom_app.ui.screen.journal.JournalScreen
import com.example.bloom_app.ui.screen.capture.CaptureScreen
import com.example.bloom_app.ui.screen.detail.DetailScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                if (navController.currentDestination?.route != "journal") {
                    navController.navigate("journal") {
                        popUpTo("auth") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            is AuthState.Unauthenticated -> {
                if (navController.currentDestination?.route != "auth") {
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            else -> Unit
        }
    }

    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        composable("auth") {
            AuthScreen(navController = navController)
        }

        composable("journal") {
            JournalScreen(navController = navController)
        }

        composable("capture") {
            CaptureScreen(navController = navController)
        }

        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            DetailScreen(navController = navController, discoveryId = id)
        }
    }
}
