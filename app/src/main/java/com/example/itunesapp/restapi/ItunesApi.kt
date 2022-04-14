package com.example.itunesapp.restapi

import com.example.itunesapp.model.Song
import com.example.itunesapp.model.Songs
import com.example.itunesapp.views.ClassicFragment
import io.reactivex.Single
import retrofit2.http.GET

interface ItunesApi {
    @GET (POP_ENDPOINT)
    fun getPopSongs() : Single<Songs>

    @GET (ROCK_ENDPOINT)
    fun getRockSongs() : Single<Songs>

    @GET (CLASSIC_ENDPOINT)
    fun getClassicalSongs() : Single<Songs>

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
        const val POP_ENDPOINT = "search?term=pop&amp;media=music&amp;entity=song&amp;limit=50"
        const val ROCK_ENDPOINT = "search?term=rock&amp;media=music&amp;entity=song&amp;limit=50"
        const val CLASSIC_ENDPOINT = "search?term=classic&amp;media=music&amp;entity=song&amp;limit=50"
    }
}