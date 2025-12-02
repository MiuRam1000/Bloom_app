package com.example.bloom_app.ui.screen.capture

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bloom_app.R
import org.koin.androidx.compose.koinViewModel
import com.example.bloom_app.ui.screen.capture.CaptureState  // ← CRITIQUE !


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureScreen(navController: NavController) {
    val viewModel: CaptureViewModel = koinViewModel()
    val context = LocalContext.current
    val captureState by viewModel.captureState.collectAsStateWithLifecycle()

    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    // Launcher photo
    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            selectedImageUri = null
            viewModel.processBitmap(context, it)
        }
    }

    // Launcher galerie
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
        uri?.let { viewModel.processImageUri(context, it) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New Discovery", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ✅ ÉTATS Capture
            when (val state = captureState) {
                CaptureState.Idle -> {
                    ImagePreview(selectedImageUri)
                    Spacer(modifier = Modifier.height(40.dp))
                    CaptureButtons(
                        onCamera = { takePhotoLauncher.launch(null) },
                        onGallery = { galleryLauncher.launch("image/*") }
                    )
                }

                CaptureState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Analyse IA en cours...", color = Color.Gray)
                    }
                }

                is CaptureState.Success -> {
                    SuccessScreen(
                        name = state.name,
                        summary = state.summary,
                        onContinue = {
                            viewModel.resetState()
                            navController.navigate("journal") {
                                popUpTo("journal") { inclusive = true }
                            }
                        }
                    )
                }

                is CaptureState.Error -> {
                    ErrorScreen(
                        message = state.message,
                        onRetry = { viewModel.resetState() }
                    )
                }
            }


        }
    }
}

// Composables helpers
@Composable
private fun ImagePreview(uri: android.net.Uri?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp))
            .border(2.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (uri != null) {
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = "Image Preview",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun CaptureButtons(onCamera: () -> Unit, onGallery: () -> Unit) {
    Column {
        Button(
            onClick = onCamera,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                painter = painterResource(android.R.drawable.ic_menu_camera),
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Capture Photo", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onGallery,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                painter = painterResource(android.R.drawable.ic_menu_gallery),
                contentDescription = null,
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Select from Gallery", color = Color.DarkGray)
        }
    }
}

@Composable
private fun SuccessScreen(name: String, summary: String, onContinue: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(android.R.drawable.star_big_on),
            contentDescription = "Success",
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = summary,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Voir Journal", color = Color.White, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(android.R.drawable.ic_dialog_alert),
            contentDescription = "Error",
            tint = Color.Red,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Réessayer")
        }
    }
}
