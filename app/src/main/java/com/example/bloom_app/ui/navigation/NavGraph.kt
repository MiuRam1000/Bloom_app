package com.example.bloom_app.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.runtime.getValue


@Composable
fun NavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = koinViewModel()
//    val authStateState = authViewModel.authState.collectAsState()
//    val authState = authStateState.value
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        Log.d("Auth", "state = $authState  current=${navController.currentDestination?.route}")

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
                        popUpTo(0) { inclusive = true }   // on vide tout le backstack
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
