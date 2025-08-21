package com.example.myscrapnel.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

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

fun extractDateFromTimestamp(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDate.format(formatter)
}


fun convertDdMmYyyyToTimestamp(dateString: String): Long? {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    try {
        val date = dateFormat.parse(dateString)
        return date?.time
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}


fun convertTimestampToDateTimeComponents(timestamp: Long): List<String> {
    val calendar = Calendar.getInstance(Locale.getDefault())
    calendar.timeInMillis = timestamp

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1
    val year = calendar.get(Calendar.YEAR)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    return listOf(
        String.format("%02d", day),
        String.format("%02d", month),
        year.toString(),
        String.format("%02d", hour),
        String.format("%02d", minute)
    )

}