package com.example.bloom_app.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ImageSaver {

    private fun getImagesDir(context: Context): File {
        val dir = File(context.filesDir, "discoveries")
        dir.mkdirs()
        return dir
    }

    /** Photo caméra → fichier interne */
    fun saveBitmap(context: Context, bitmap: Bitmap): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(getImagesDir(context), "discovery_$timestamp.jpg")

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file.absolutePath
    }

    /** Galerie → fichier interne */
    fun saveFromUri(context: Context, uri: Uri): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val file = File(getImagesDir(context), "discovery_$timestamp.jpg")

                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
                file.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
