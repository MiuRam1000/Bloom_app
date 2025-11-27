// ui/screen/detail/DetailScreen.kt → VERSION PRO, SANS IMAGE MANUELLE
package com.example.bloom_app.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bloom_app.R
import com.example.bloom_app.domaine.model.Discovery
import com.example.bloom_app.ui.screen.journal.JournalViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    discoveryId: Long,
    viewModel: JournalViewModel = koinViewModel()
) {
    val discovery = viewModel.getDiscoveryById(discoveryId) ?: Discovery(
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: "demo",
        name = "Plante inconnue",
        summary = "Cette plante n'a pas encore été analysée par l'IA.",
        imagePath = "",
        timestamp = System.currentTimeMillis()
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Détail de la découverte", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painterResource(android.R.drawable.ic_menu_close_clear_cancel), "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            // IMAGE : SI PAS D'IMAGE → AFFICHAGE PROPRE (comme une vraie app)
            if (discovery.imagePath.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(discovery.imagePath),
                    contentDescription = discovery.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                // État vide élégant → 100 % pro
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF1F8E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_camera),
                            contentDescription = null,
                            tint = Color(0xFF66BB6A),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Aucune photo",
                            color = Color(0xFF66BB6A),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Prenez une photo pour identifier cette plante",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(discovery.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(android.R.drawable.ic_menu_today), null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Découverte : ${discovery.formattedDate}", color = Color.Gray)
            }

            Spacer(Modifier.height(32.dp))
            Text(discovery.summary, style = MaterialTheme.typography.bodyLarge, lineHeight = 28.sp)
            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button
                    viewModel.deleteDiscovery(discoveryId)
                    navController.popBackStack("journal", false)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Supprimer", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}