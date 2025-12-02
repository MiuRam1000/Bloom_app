package com.example.bloom_app.domaine.usecase

import android.graphics.BitmapFactory
import com.example.bloom_app.util.GeminiParser
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import com.google.ai.client.generativeai.type.content

class AnalyzeImageWithGeminiUseCase(
    private val generativeModel: GenerativeModel  // ✅ Injecté par Koin !
) {
    suspend operator fun invoke(imagePath: String): Pair<String, String>? = withContext(Dispatchers.IO) {
        try {
            // 1) Charger l'image en BITMAP (syntaxe correcte Gemini Android)
            val imageFile = File(imagePath)
            if (!imageFile.exists()) return@withContext null

            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            if (bitmap == null) return@withContext null

            // 2) Prompt specs PDF
            val prompt = """
                Identifiez cet objet et écrivez un fait amusant de deux phrases à son sujet.
                Fournissez d'abord le nom, puis le fait.
                
                Format exact :
                Nom de l'objet
                
                Fait amusant (2 phrases max).
            """.trimIndent()

            // 3) SYNTAXE CORRECTE Gemini Android SDK
            val content = content {
                text(prompt)
                image(bitmap)  // ← BITMAP directement !
            }

            // 4) Appel Gemini
            val response = generativeModel.generateContent(content)
            val text = response.text ?: return@withContext null

            // 5) Ton parser
            GeminiParser.parseResponse(text)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
