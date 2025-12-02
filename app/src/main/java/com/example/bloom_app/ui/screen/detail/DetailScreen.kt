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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bloom_app.R
import com.example.bloom_app.domaine.model.Discovery
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    discoveryId: Long,
    viewModel: DetailViewModel = koinViewModel()
) {
    // ✅ CHARGE la discovery au démarrage
    LaunchedEffect(discoveryId) {
        viewModel.loadDiscovery(discoveryId)
    }

    // ✅ Attend le Flow réactif
    val discovery by viewModel.discovery.collectAsStateWithLifecycle()
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()

    // ✅ Navigation après suppression
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            navController.popBackStack("journal", false)
        }
    }

    // ✅ Loading state
    if (discovery == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Détail de la découverte", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painterResource(android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = "Retour"
                        )
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
            // ✅ SAFE ACCESS (let au lieu de !!)
            discovery?.let { safeDiscovery ->
                if (safeDiscovery.imagePath.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(safeDiscovery.imagePath),
                        contentDescription = safeDiscovery.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
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
                Text(
                    text = safeDiscovery.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(android.R.drawable.ic_menu_today),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Découverte : ${safeDiscovery.formattedDate}",
                        color = Color.Gray
                    )
                }

                Spacer(Modifier.height(32.dp))
                Text(
                    text = safeDiscovery.summary,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 28.sp
                )
                Spacer(Modifier.weight(1f))

                Button(
                    onClick = { viewModel.deleteDiscovery(discoveryId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Supprimer",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
