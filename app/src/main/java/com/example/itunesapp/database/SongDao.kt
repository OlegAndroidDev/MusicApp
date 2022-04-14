package com.example.itunesapp.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.itunesapp.model.Song
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface SongDao {
    @Query("SELECT * FROM song")
    fun getAll() : Single<List<Song>>

    @Query("SELECT * FROM song WHERE mainGenre LIKE (:genre)")
    fun getAllByGenre(genre: String) : Single<List<Song>>

    @Insert(onConflict = REPLACE)
    fun insertSong(song: Song) : Completable

    @Insert(onConflict = REPLACE)
    fun insertAll(songs: List<Song>) : Completable

    @Delete
    fun deleteSong(song: Song) : Completable
}