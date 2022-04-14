package com.example.itunesapp

import android.app.Application
import com.example.itunesapp.di.ApplicationModule
import com.example.itunesapp.di.DaggerSongComponent
import com.example.itunesapp.di.SongComponent

class ItunesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        songComponent = DaggerSongComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    companion object {
        lateinit var songComponent : SongComponent
    }
}