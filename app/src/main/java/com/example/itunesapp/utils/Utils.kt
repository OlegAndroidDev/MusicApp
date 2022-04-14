package com.example.itunesapp.utils

import android.media.MediaPlayer
import com.example.itunesapp.model.Song

fun playMusic(song: Song) {
    val player = MediaPlayer()
    player.stop()
    player.reset()
    player.setDataSource(song.previewUrl)
    player.prepare()
    player.start()
}