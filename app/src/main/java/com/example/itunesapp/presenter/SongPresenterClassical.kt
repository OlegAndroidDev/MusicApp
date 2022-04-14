package com.example.itunesapp.presenter

import android.content.Context
import android.util.Log
import com.example.itunesapp.database.DatabaseRepository
import com.example.itunesapp.database.SongDao
import com.example.itunesapp.database.SongDatabase
import com.example.itunesapp.model.Song
import com.example.itunesapp.model.Songs
import com.example.itunesapp.restapi.SongRepository
import com.example.itunesapp.utils.NetworkMonitor
import com.example.itunesapp.utils.NetworkState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SongPresenterClassical @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val songRepository: SongRepository,
    private val networkMonitor: NetworkMonitor, // = NetworkMonitor(context),
    private val disposable: CompositeDisposable // = CompositeDisposable()
) : SongPresenterClassicalContract{

    private var songViewContract: SongViewContract? = null

    override fun initializePresenter(viewContract: SongViewContract) {
        songViewContract = viewContract
    }

    override fun getClassicSongs() {
        songViewContract?.loadingSongs(true)

        networkMonitor.networkState
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { netstate -> if (netstate) {
                    doNetworkCallClassical()
                }
                else {
                    offlineLoadFromDatabase()
//                    songViewContract?.songFailed(Throwable("ERROR NO INTERNET CONNECTION"))
                }},
                { error ->
                    offlineLoadFromDatabase()
//                    songViewContract?.songFailed(error)
                  }
            )
            .apply {
                disposable.add(this)
            }
    }

    override fun destroy() {
        networkMonitor.unregisterNetworkMonitor()
        songViewContract = null
        disposable.dispose()
    }

    override fun checkNetwork() {
        networkMonitor.registerNetworkMonitor()
    }

    private fun doNetworkCallClassical() {
        songRepository.getClassicalSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { songs ->
                    insertClassicSongsToDatabase(songs.songs)
                },
                { error ->
                    offlineLoadFromDatabase()
                    songViewContract?.songFailed(error) }
            ).apply {
                disposable.add(this)
            }
    }

    private fun insertClassicSongsToDatabase(songs: List<Song>) {
        songs.forEach{
            it.genre = GENRE
            removeEmptyFields(it)
        }

        databaseRepository.insertAll(songs)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { getClassicSongsFromDatabase()}
            ).apply {
                disposable.add(this)
            }
    }
    private fun getClassicSongsFromDatabase() {
        databaseRepository.getAllByGenre(GENRE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { songs ->
                    songViewContract?.songSuccess(songs)
                    Log.d("Classic fragment", "classic songs loaded") },
                { error ->
                    songViewContract?.songFailed(error)
                    Log.e("Classic fragment", error.toString()) }
            ).apply {
                disposable.add(this)
            }
    }
    private fun offlineLoadFromDatabase() {
        databaseRepository.getAllByGenre(GENRE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { songs ->
                    songViewContract?.offlineLoad(songs) },
                { error ->
                    songViewContract?.songFailed(error)
                }
            ).apply {
                disposable.add(this)
            }
    }

    private fun removeEmptyFields(song: Song) : Song {
        if (song.contentAdvisoryRating.isNullOrEmpty()) {
            song.contentAdvisoryRating = ""
        }
        if (song.artworkUrl30.isNullOrEmpty()) {
            song.artworkUrl30 = ""
        }
        if (song.kind.isNullOrEmpty()) {
            song.kind = ""
        }
        if (song.trackCensoredName.isNullOrEmpty()) {
            song.trackCensoredName = ""
        }
        if (song.trackExplicitness.isNullOrEmpty()) {
            song.trackExplicitness = ""
        }
        if (song.trackName.isNullOrEmpty()) {
            song.trackName = ""
        }
        if (song.trackViewUrl.isNullOrEmpty()) {
            song.trackViewUrl = ""
        }
        if (song.artistViewUrl.isNullOrEmpty()) {
            song.artistViewUrl = ""
        }
        return song
    }

    companion object {
        const val GENRE = "classic"
    }
}
interface SongPresenterClassicalContract {
    fun initializePresenter(viewContract: SongViewContract)
    fun getClassicSongs()
    fun destroy()
    fun checkNetwork()
}

interface SongViewContract {
    fun offlineLoad(songs: List<Song>)
    fun loadingSongs(isLoading: Boolean)
    fun songSuccess(songs: List<Song>)
    fun songFailed(throwable: Throwable)
}