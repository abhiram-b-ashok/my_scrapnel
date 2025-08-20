package com.example.myscrapnel.room_db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScrapnelEntity::class], version = 1)
abstract class ScrapnelDatabase : RoomDatabase() {

    abstract fun dao(): ScrapnelDao

}
