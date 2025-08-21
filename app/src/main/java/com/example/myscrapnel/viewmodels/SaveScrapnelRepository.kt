package com.example.myscrapnel.viewmodels

import com.example.myscrapnel.room_db.ScrapnelDao
import com.example.myscrapnel.room_db.ScrapnelEntity


class SaveScrapnelRepository(private val dao: ScrapnelDao) {
    var existingSimilarTimeStamp: Long?= null
    suspend fun isScrapnelWithinFiveMinutes(newTimestamp: Long): Boolean {
        val allTimestamps = dao.getAllTimestamps()
        val fiveMinutes = 5 * 60 * 1000 

        for (existingTimestamp in allTimestamps) {
            val difference = kotlin.math.abs(existingTimestamp - newTimestamp)
            if (difference <= fiveMinutes) {
                existingSimilarTimeStamp = existingTimestamp
                return true

            }
        }

        return false 
    }

    suspend fun insertScrapnel(scrapnel: ScrapnelEntity) {
        dao.insertScrapnel(scrapnel)
    }
    suspend fun getTheScrapnelToEdit(timeStamp: Long): ScrapnelEntity? {
        val scrapnel = dao.getScrapnelByTimestamp(timeStamp)
        return scrapnel
    }
    suspend fun updateScrapnel(scrapnel: ScrapnelEntity) {
        dao.updateScrapnel(scrapnel)
    }


}
