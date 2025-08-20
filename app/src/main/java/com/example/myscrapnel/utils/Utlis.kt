package com.example.myscrapnel.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.InputStream
import java.util.Calendar

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

fun getTimestamp(year: String, month: String, day: String, hour: String, minute: String): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year.toInt())
        set(Calendar.MONTH, month.toInt() - 1)
        set(Calendar.DAY_OF_MONTH, day.toInt())
        set(Calendar.HOUR_OF_DAY, hour.toInt())
        set(Calendar.MINUTE, minute.toInt())
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}