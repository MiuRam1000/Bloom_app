// domaine/model/Discovery.kt
package com.example.bloom_app.domaine.model

import com.example.bloom_app.util.DateUtils

data class Discovery(
    val id: Long = 0,
    val userId: String,
    val name: String,
    val summary: String,
    val imagePath: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    val formattedDate: String
        get() = DateUtils.formatTimestamp(timestamp)
}