// ui/navigation/AppNavGraph.kt
package com.example.bloom_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bloom_app.ui.screen.auth.AuthScreen
import com.example.bloom_app.ui.screen.journal.JournalScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") { AuthScreen(navController) }
        composable("journal") { JournalScreen(navController) }
        composable("capture") { JournalScreen(navController) } // Temporaire
        composable("detail/{id}") { JournalScreen(navController) } // Temporaire
    }
}