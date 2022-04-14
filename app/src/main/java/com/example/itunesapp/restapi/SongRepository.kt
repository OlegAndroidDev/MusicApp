package com.example.itunesapp.restapi

import com.example.itunesapp.model.Song
import com.example.itunesapp.model.Songs
import io.reactivex.Single
import javax.inject.Inject

interface SongRepository {
    fun getClassicalSongs() : Single<Songs>
    fun getRockSongs() : Single<Songs>
    fun getPopSongs() : Single<Songs>
}

class SongRepositoryImpl @Inject constructor(
    private val songApi: ItunesApi
) : SongRepository {
    override fun getClassicalSongs(): Single<Songs> {
        return songApi.getClassicalSongs()
    }

    override fun getRockSongs(): Single<Songs> {
        return songApi.getRockSongs()
    }

    override fun getPopSongs(): Single<Songs> {
        return songApi.getPopSongs()
    }
}