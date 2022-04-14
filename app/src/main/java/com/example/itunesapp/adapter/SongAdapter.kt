package com.example.itunesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesapp.R
import com.example.itunesapp.model.Song
import com.example.itunesapp.model.Songs
import com.squareup.picasso.Picasso

class SongAdapter(
    private val songs: MutableList<Song> = mutableListOf(),
    private val onSongClicked: (Song) -> Unit
) : RecyclerView.Adapter<SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val songView = LayoutInflater.from(parent.context)
            .inflate(R.layout.song_item, parent, false)
        return SongViewHolder(songView, onSongClicked)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int = songs.size

    fun updateSongs(newSongs: List<Song>) {
        songs.clear()
        songs.addAll(newSongs)
        notifyDataSetChanged()
    }
}

class SongViewHolder(
    itemView: View,
    private val onSongClicked: (Song) -> Unit
) : RecyclerView.ViewHolder(itemView){

    private val songName : TextView = itemView.findViewById(R.id.artistTitle)
    private val songPrice : TextView = itemView.findViewById(R.id.price)
    private val artistName : TextView = itemView.findViewById(R.id.artistName)
    private val artistImage : ImageView = itemView.findViewById(R.id.artistImage)

    fun bind(song: Song) {
        songName.text = song.trackName
        artistName.text = song.artistName

        songPrice.text = "\$${song.trackPrice.toString()}"
        itemView.setOnClickListener {
            onSongClicked.invoke(song)
        }
        Picasso.get()
            .load(song.artworkUrl100)
            .placeholder(R.drawable.ic_baseline_downloading)
            .error(R.drawable.ic_baseline_error_outline)
            .fit()
            .into(artistImage)
    }

}