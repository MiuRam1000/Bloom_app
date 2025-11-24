// util/ImageSaver.kt
package com.example.bloom_app.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import java.io.File
import java.io.FileOutputStream

object ImageSaver {

    private fun getPhotosDirectory(context: Context): File {
        val dir = File(context.filesDir, "bloom_discoveries")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun saveBitmap(context: Context, bitmap: Bitmap): String {
        val filename = "discovery_${System.currentTimeMillis()}.jpg"
        val file = File(getPhotosDirectory(context), filename)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        }
        return file.absolutePath
    }

    fun saveFromUri(context: Context, uri: Uri): String? {
        return try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.createSource(context.contentResolver, uri)
                    .let { ImageDecoder.decodeBitmap(it) }
            } else {
                @Suppress("DEPRECATION")
                android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            saveBitmap(context, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}