package com.example.bloom_app.domaine.usecase

import android.graphics.BitmapFactory
import com.example.bloom_app.util.GeminiParser
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import com.google.ai.client.generativeai.type.content

class GeminiAnalyzeImageUseCase(
    private val generativeModel: GenerativeModel
) : AnalyzeImageUseCase {  // ✅ INTERFACE !

    override suspend fun invoke(imagePath: String): Pair<String, String>? = withContext(Dispatchers.IO) {
        // Ton code RESTE IDENTIQUE ✅
        try {
            val imageFile = File(imagePath)
            if (!imageFile.exists()) return@withContext null

            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            if (bitmap == null) return@withContext null

            val prompt = """
                Identifiez cet objet et écrivez un fait amusant de deux phrases à son sujet.
                Fournissez d'abord le nom, puis le fait.
                
                Format exact :
                Nom de l'objet
                
                Fait amusant (2 phrases max).
            """.trimIndent()

            val content = content {
                text(prompt)
                image(bitmap)
            }

            val response = generativeModel.generateContent(content)
            val text = response.text ?: return@withContext null

            GeminiParser.parseResponse(text)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
