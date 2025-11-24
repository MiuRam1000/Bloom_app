// util/DateUtils.kt
package com.example.bloom_app.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val displayFormat = SimpleDateFormat("dd MMM yyyy 'à' HH:mm", Locale.getDefault())

    fun formatTimestamp(timestamp: Long): String {
        return displayFormat.format(Date(timestamp))
    }
}