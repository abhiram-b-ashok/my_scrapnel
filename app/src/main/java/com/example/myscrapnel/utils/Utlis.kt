package com.example.myscrapnel.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.InputStream

fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val fileName = "scrapnel_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        file.absolutePath

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
