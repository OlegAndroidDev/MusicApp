package com.example.itunesapp.adapter

import com.example.itunesapp.model.Song

interface SongClicked {
    fun onSongClicked(song: Song)
}