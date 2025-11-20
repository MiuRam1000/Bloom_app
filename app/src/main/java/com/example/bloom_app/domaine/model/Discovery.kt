package com.example.bloom_app.domaine.model

import java.text.SimpleDateFormat
import java.util.*

data class Discovery(
    val id: Long = 0,
    val userId: String,
    val name: String,
    val summary: String,
    val imagePath: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun formattedDate(): String =
        SimpleDateFormat("dd MMM yyyy à HH:mm", Locale.getDefault()).format(Date(timestamp))
}