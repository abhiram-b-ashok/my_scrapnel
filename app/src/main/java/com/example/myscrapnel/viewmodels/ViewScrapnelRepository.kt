package com.example.myscrapnel.viewmodels

import com.example.myscrapnel.models.scrapnel_tf_content.ScrapnelUiModel
import com.example.myscrapnel.room_db.ScrapnelDao


class ViewScrapnelRepository(private val dao: ScrapnelDao) {

    suspend fun getAllScrapnelUiModels(): List<ScrapnelUiModel> {
        val entities = dao.getAllScrapnel()

        return entities.map { entity ->
            val lines = entity.content.lines()

            val imageLines = lines.filter { it.startsWith("🖼️ file://") }
            val firstImageUri = imageLines.firstOrNull()?.removePrefix("🖼️ ") // optional fallback to "" if needed

            val textLines = lines.filterNot { it.startsWith("🖼️ file://") }
            val cleanText = textLines.joinToString("\n").trim()

            ScrapnelUiModel(
                title = entity.title,
                fullText = cleanText,
                firstImageUri = firstImageUri,
                timeStamp = entity.timeStamp,
                createdAt = entity.createdAt
            )
        }
    }
}
