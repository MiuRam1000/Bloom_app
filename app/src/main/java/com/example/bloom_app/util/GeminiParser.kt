// util/GeminiParser.kt
package com.example.bloom_app.util

object GeminiParser {

    // Gemini doit répondre exactement comme ça :
    // Monstera Deliciosa
    //
    // Cette plante iconique est surnommée "fromage suisse" à cause de ses feuilles trouées. Elle adore la lumière indirecte et purifie l'air !

    fun parseResponse(response: String): Pair<String, String>? {
        val trimmed = response.trim()
        val lines = trimmed.lines()

        if (lines.isEmpty()) return null

        val name = lines[0].trim()
        val summaryLines = lines.dropWhile { it.isBlank() || it.trim() == name }
        val summary = summaryLines.joinToString(" ").trim()

        return if (name.isNotBlank() && summary.isNotBlank()) {
            name to summary
        } else null
    }
}