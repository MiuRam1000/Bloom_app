// MainActivity.kt
package com.example.bloom_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.bloom_app.ui.navigation.NavGraph
import com.example.bloom_app.ui.theme.Bloom_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bloom_appTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}