package com.example.itunesapp.database

import com.example.itunesapp.model.Song
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

interface DatabaseRepository {
    fun getAll() : Single<List<Song>>
    fun getAllByGenre(genre : String) : Single<List<Song>>
    fun insertSong(song: Song) : Completable
    fun insertAll(songs: List<Song>) : Completable
    fun deleteSong(song: Song) : Completable
}

class DatabaseRepositoryImpl @Inject constructor(
    private val dao: SongDao
) : DatabaseRepository {
    override fun getAll(): Single<List<Song>> = dao.getAll()

    override fun getAllByGenre(genre : String): Single<List<Song>> =  dao.getAllByGenre(genre)

    override fun insertSong(song: Song): Completable = dao.insertSong(song)

    override fun insertAll(songs: List<Song>): Completable = dao.insertAll(songs)

    override fun deleteSong(song: Song): Completable = dao.deleteSong(song)
}