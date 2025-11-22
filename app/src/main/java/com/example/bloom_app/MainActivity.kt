// MainActivity.kt
package com.example.bloom_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bloom_app.ui.screen.journal.JournalScreen
import com.example.bloom_app.ui.theme.Bloom_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bloom_appTheme {
                // Pour la Semaine 1 : Affiche directement le JournalScreen
                JournalScreen(navController = rememberNavController())
                JournalScreen(navController = rememberNavController())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Bloom_appTheme {
        JournalScreen(navController = rememberNavController())
    }
}