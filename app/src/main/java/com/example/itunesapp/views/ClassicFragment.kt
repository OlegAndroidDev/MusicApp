package com.example.itunesapp.views

import android.app.AlertDialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itunesapp.ItunesApp
import com.example.itunesapp.R
import com.example.itunesapp.adapter.SongAdapter
import com.example.itunesapp.database.SongDatabase
import com.example.itunesapp.databinding.FragmentClassicBinding
import com.example.itunesapp.model.Song
import com.example.itunesapp.model.Songs
import com.example.itunesapp.presenter.SongPresenterClassical
import com.example.itunesapp.presenter.SongViewContract
import com.example.itunesapp.utils.playMusic
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class ClassicFragment() : Fragment(), SongViewContract {

    @Inject
    lateinit var songPresenterClassical: SongPresenterClassical

    private val player by lazy {
        MediaPlayer()
    }

    private val binding by lazy {
        FragmentClassicBinding.inflate(layoutInflater)
    }

    private val songAdapter by lazy {
        SongAdapter(onSongClicked = {
            player.stop()
            player.reset()
            player.setDataSource(it.previewUrl)
            player.prepare()
            player.start()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ItunesApp.songComponent.inject(this)
        songPresenterClassical.initializePresenter(this)
        binding.myRecycler.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = songAdapter
        }
        songPresenterClassical.checkNetwork()
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        songPresenterClassical.getClassicSongs()
        binding.swipeToRefresh.setOnRefreshListener {
            songPresenterClassical.getClassicSongs()
            binding.swipeToRefresh.isRefreshing = true
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()

        songPresenterClassical.destroy()
    }
    override fun offlineLoad(songs: List<Song>) {
        binding.myRecycler.visibility = View.VISIBLE
        binding.loadingBar.visibility = View.GONE
        songAdapter.updateSongs(songs)
        binding.swipeToRefresh.isRefreshing = false
    }
    override fun loadingSongs(isLoading: Boolean) {
        binding.myRecycler.visibility = View.GONE
        binding.loadingBar.visibility = View.VISIBLE
    }
    override fun songSuccess(songs: List<Song>) {
        binding.myRecycler.visibility = View.VISIBLE
        binding.loadingBar.visibility = View.GONE
        songAdapter.updateSongs(songs)
        binding.swipeToRefresh.isRefreshing = false
    }
    override fun songFailed(throwable: Throwable) {
        binding.myRecycler.visibility = View.GONE
        binding.loadingBar.visibility = View.GONE

        AlertDialog.Builder(requireContext())
            .setTitle("AN ERROR HAS OCCURRED")
            .setMessage(throwable.localizedMessage)
            .setPositiveButton("DISMISS") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    companion object {
        fun newInstance() =
            ClassicFragment()
    }
}

