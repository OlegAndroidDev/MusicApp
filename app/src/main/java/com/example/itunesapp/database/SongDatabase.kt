package com.example.itunesapp.database

import androidx.room.*
import androidx.room.RoomDatabase
import com.example.itunesapp.model.Song

@Database(entities = [Song::class], version = 1)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao() : SongDao
}