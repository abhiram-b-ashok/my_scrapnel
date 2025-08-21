package com.example.myscrapnel.viewmodels

import com.example.myscrapnel.models.scrapnel_ui_model.ScrapnelUiModel
import com.example.myscrapnel.room_db.ScrapnelDao
import com.example.myscrapnel.room_db.ScrapnelEntity


class ViewScrapnelRepository(private val dao: ScrapnelDao) {

    suspend fun getAllScrapnelUiModels(): List<ScrapnelUiModel> {
        val allScrapnels = dao.getAllScrapnel()

        return allScrapnels.map { entity ->
            val lines = entity.content.lines()

            val imageUris = mutableListOf<String>()
            val textLines = mutableListOf<String>()

            lines.forEach { line ->
                if (line.isBlank()) return@forEach

                if (line.startsWith("🖼️")) {
                    imageUris.add(line.removePrefix("🖼️ ").trim())
                } else {
                    textLines.add(line)
                }
            }

            ScrapnelUiModel(
                title = entity.title,
                fullText = textLines.joinToString("\n").trim(),
                firstImageUri = imageUris.firstOrNull(),
                timeStamp = entity.timeStamp,
                createdAt = entity.createdAt
            )
        }
    }

    suspend fun getFilteredScrapnels(filterKey: String): List<ScrapnelUiModel> {

        val filteredScrapnels = dao.searchScrapnels(filterKey)

        return filteredScrapnels.map { entity ->
            val lines = entity.content.lines()
            val imageUris = mutableListOf<String>()
            val textLines = mutableListOf<String>()
            lines.forEach { line ->
                if (line.isBlank()) return@forEach

                if (line.startsWith("🖼️")) {
                    imageUris.add(line.removePrefix("🖼️ ").trim())
                } else {
                    textLines.add(line)
                }
            }

            ScrapnelUiModel(
                title = entity.title,
                fullText = textLines.joinToString("\n").trim(),
                firstImageUri = imageUris.firstOrNull(),
                timeStamp = entity.timeStamp,
                createdAt = entity.createdAt
            )


        }
    }


    suspend fun deleteScrapnel(scrapnel: List<ScrapnelEntity>) {
        scrapnel.forEach { entity ->
            dao.deleteScrapnel(entity)
        }
    }


}

