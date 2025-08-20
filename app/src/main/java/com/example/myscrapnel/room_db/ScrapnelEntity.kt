package com.example.myscrapnel.room_db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "scrapnel")
data class ScrapnelEntity(
    @PrimaryKey val timeStamp: Long,
    val title: String,
    val content: String,
    val createdAt: Long,
)

