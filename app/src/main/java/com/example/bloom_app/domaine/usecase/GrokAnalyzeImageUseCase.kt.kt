package com.example.bloom_app.domaine.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.bloom_app.util.GeminiParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class GrokAnalyzeImageUseCase(
    private val apiKey: String
) : AnalyzeImageUseCase {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    override suspend fun invoke(imagePath: String): Pair<String, String>? = withContext(Dispatchers.IO) {
        try {
            println("🔄 Grok: Début analyse $imagePath")

            // 1. Image → Base64
            val bitmap = BitmapFactory.decodeFile(imagePath) ?: return@withContext null
            val base64Image = bitmapToBase64(bitmap)
            println("🔄 Grok: Image base64 OK (${base64Image.length} chars)")

            // 2. JSON Grok Vision (même format OpenAI !)
            val json = JSONObject().apply {
                put("model", "meta-llama/llama-4-scout-17b-16e-instruct")
                put("messages", JSONArray().put(
                    JSONObject().apply {
                        put("role", "user")
                        put("content", JSONArray().apply {
                            put(JSONObject().apply {
                                put("type", "text")
                                put("text", "Identifiez cette plante et donnez un fait amusant (2 phrases). Format:\nNOM\n\nFAIT")
                            })
                            put(JSONObject().apply {
                                put("type", "image_url")
                                put("image_url", JSONObject().apply {
                                    put("url", "data:image/jpeg;base64,$base64Image")
                                })
                            })
                        })
                    }
                ))
                put("max_tokens", 150)
            }

            // 3. Requête Grok (compatible OpenAI)
            val request = Request.Builder()
                .url("https://api.groq.com/openai/v1/chat/completions")  // ✅ Grok URL
                .header("Authorization", "Bearer $apiKey")
                .post(json.toString().toRequestBody("application/json".toMediaTypeOrNull()))
                .build()

            // 4. Réponse
            println("🔄 Grok: Envoi requête...")
            val response = client.newCall(request).execute()

            println("🔄 Grok: Réponse ${response.code}: ${response.message}")

            if (!response.isSuccessful) {
                println("❌ Grok Error: ${response.code} - ${response.body?.string()}")
                return@withContext null
            }

            val content = JSONObject(response.body!!.string())
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")

            GeminiParser.parseResponse(content)

        } catch (e: Exception) {
            println("❌ Grok Exception: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
    }
}
