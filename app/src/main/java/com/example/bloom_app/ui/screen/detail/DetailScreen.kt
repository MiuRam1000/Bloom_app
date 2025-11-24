// ui/screen/detail/DetailScreen.kt
package com.example.bloom_app.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bloom_app.domaine.model.Discovery
import com.example.bloom_app.R
import com.example.bloom_app.ui.screen.journal.JournalViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    discoveryId: Long,
    viewModel: JournalViewModel = koinViewModel()
) {
    // Récupère la découverte depuis le ViewModel (ou fake si pas encore implémenté)
    val discovery = viewModel.getDiscoveryById(discoveryId)
        ?: Discovery(
            id = discoveryId,
            name = "Monstera Deliciosa",
            date = "November 15, 2023 at 02:45 PM",
            imagePath = "", // sera remplacé par le vrai chemin
            aiFact = "The Monstera deliciosa, also known as the Swiss cheese plant, is a species of flowering plant native to tropical forests of southern Mexico, south to Panama. It is renowned for its large, glossy, heart-shaped leaves that develop natural holes or fenestrations as they mature. These striking features make it a popular houseplant, often grown for its ornamental appeal. The plant can also produce edible fruit, though it rarely fruits indoors. It thrives in warm, humid conditions with indirect light."
        )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Discovery Details", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Share */ }) {
                        Icon(painter = painterResource(android.R.drawable.ic_menu_share), contentDescription = "Share")
                    }
                    IconButton(onClick = { /* TODO: Edit */ }) {
                        Icon(painter = painterResource(android.R.drawable.ic_menu_edit), contentDescription = "Edit")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            // Image principale
            Image(
                painter = if (discovery.imagePath.isNotEmpty()) {
                    rememberAsyncImagePainter(discovery.imagePath)
                } else {
                    painterResource(R.drawable.monstera) // image temporaire
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nom de la plante
            Text(
                text = discovery.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date de découverte
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_menu_today),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Discovered: ${discovery.date}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Fait IA
            Text(
                text = discovery.aiFact,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // Bouton Delete Entry
            Button(
                onClick = {
                    viewModel.deleteDiscovery(discoveryId)
                    navController.popBackStack("journal", false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Delete Entry", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}