package com.example.bloom_app.ui.screen.capture

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bloom_app.domaine.model.Discovery
import com.example.bloom_app.domaine.usecase.AddDiscoveryUseCase
import com.example.bloom_app.domaine.usecase.AnalyzeImageWithGeminiUseCase
import com.example.bloom_app.util.ImageSaver
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CaptureState {
    object Idle : CaptureState()
    object Loading : CaptureState()
    data class Success(val name: String, val summary: String) : CaptureState()
    data class Error(val message: String) : CaptureState()
}

class CaptureViewModel(
    private val analyzeImageUseCase: AnalyzeImageWithGeminiUseCase,
    private val addDiscoveryUseCase: AddDiscoveryUseCase
) : ViewModel() {

    private val _captureState = MutableStateFlow<CaptureState>(CaptureState.Idle)
    val captureState: StateFlow<CaptureState> = _captureState

    // Photo prise avec la caméra (bitmap)
    fun processBitmap(context: Context, bitmap: Bitmap) {
        viewModelScope.launch {
            _captureState.value = CaptureState.Loading
            try {
                val imagePath = ImageSaver.saveBitmap(context, bitmap)
                analyzeAndSaveImage(imagePath)
            } catch (e: Exception) {
                _captureState.value = CaptureState.Error(e.message ?: "Bitmap processing failed")
            }
        }
    }

    // Image choisie depuis la galerie (URI)
    fun processImageUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            _captureState.value = CaptureState.Loading
            try {
                val imagePath = ImageSaver.saveFromUri(context, uri)
                if (imagePath == null) {
                    _captureState.value = CaptureState.Error("Failed to save image")
                    return@launch
                }
                analyzeAndSaveImage(imagePath)
            } catch (e: Exception) {
                _captureState.value = CaptureState.Error(e.message ?: "URI processing failed")
            }
        }
    }

    // Appel Gemini + sauvegarde en base
    private suspend fun analyzeAndSaveImage(imagePath: String) {
        try {
            val result = analyzeImageUseCase(imagePath)
            if (result == null) {
                _captureState.value = CaptureState.Error("Gemini analysis failed")
                return
            }

            val (name, summary) = result

            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "temp_user_id"
            val discovery = Discovery(
                userId = userId,
                name = name,
                summary = summary,
                imagePath = imagePath,
                timestamp = System.currentTimeMillis()
            )

            addDiscoveryUseCase(discovery)
            _captureState.value = CaptureState.Success(name, summary)
        } catch (e: Exception) {
            _captureState.value = CaptureState.Error(e.message ?: "Unknown error")
        }
    }

    fun resetState() {
        _captureState.value = CaptureState.Idle
    }
}
