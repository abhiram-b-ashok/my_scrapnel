package com.example.myscrapnel.room_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScrapnelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScrapnel(scrapnel: ScrapnelEntity): Long

    @Delete
    suspend fun deleteScrapnel(scrapnel: ScrapnelEntity)

    @Update
    suspend fun updateScrapnel(scrapnel: ScrapnelEntity)

    @Query("SELECT * FROM scrapnel ORDER BY timeStamp DESC")
    suspend fun getAllScrapnel(): List<ScrapnelEntity>

    @Query("SELECT title FROM scrapnel ORDER BY createdAt DESC")
    suspend fun getScrapnelTitleChips(): List<String>

    @Query("SELECT * FROM scrapnel WHERE timeStamp = :timeStamp")
    suspend fun getScrapnelByTimeStamp(timeStamp: Long): ScrapnelEntity?

    @Query("SELECT * FROM scrapnel WHERE title = :title")
    suspend fun getScrapnelByTitle(title: String): ScrapnelEntity?

    @Query("SELECT timeStamp FROM scrapnel")
    suspend fun getAllTimestamps(): List<Long>


}





