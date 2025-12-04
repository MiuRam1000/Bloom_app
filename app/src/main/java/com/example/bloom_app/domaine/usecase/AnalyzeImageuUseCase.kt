package com.example.bloom_app.domaine.usecase

interface AnalyzeImageUseCase {
    suspend operator fun invoke(imagePath: String): Pair<String, String>?
}