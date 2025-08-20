package com.example.myscrapnel.viewmodels

import com.example.myscrapnel.room_db.ScrapnelDao
import com.example.myscrapnel.room_db.ScrapnelEntity


class SaveScrapnelRepository(private val dao: ScrapnelDao) {
    suspend fun isScrapnelWithinFiveMinutes(newTimestamp: Long): Boolean {
        val allTimestamps = dao.getAllTimestamps()
        val fiveMinutes = 5 * 60 * 1000 

        for (existingTimestamp in allTimestamps) {
            val difference = kotlin.math.abs(existingTimestamp - newTimestamp)
            if (difference < fiveMinutes) {
                return true 
            }
        }

        return false 
    }

    suspend fun insertScrapnel(scrapnel: ScrapnelEntity) {
        dao.insertScrapnel(scrapnel)
    }
    suspend fun getAllScrapnel(): List<ScrapnelEntity> {
        return dao.getAllScrapnel()
    }
}
