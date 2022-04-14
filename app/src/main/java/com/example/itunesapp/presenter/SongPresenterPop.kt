package com.example.itunesapp.presenter

import android.util.Log
import com.example.itunesapp.database.DatabaseRepository
import com.example.itunesapp.database.SongDatabase
import com.example.itunesapp.model.Song
import com.example.itunesapp.model.Songs
import com.example.itunesapp.restapi.SongRepository
import com.example.itunesapp.utils.NetworkMonitor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SongPresenterPop @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val songRepository: SongRepository,
    private val networkMonitor: NetworkMonitor,
    private val disposable: CompositeDisposable
) : SongPresenterPopContract {

    private var songViewContract: SongViewContract? = null

    override fun initializePresenter(viewContract: SongViewContract) {
        songViewContract = viewContract
    }

    override fun getPopSongs() {
        songViewContract?.loadingSongs(true)

        networkMonitor.networkState
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { netstate -> if (netstate) {
                    doNetworkCallPop()
                }
                else {
                    offlineLoadFromDatabase()
//                    songViewContract?.songFailed(Throwable("ERROR NO INTERNET CONNECTION")
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

    private fun doNetworkCallPop() {
        songRepository.getPopSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { songs ->
                    insertPopSongsToDatabase(songs.songs)
                },
                { error ->
                    offlineLoadFromDatabase()
//                    songViewContract?.songFailed(error)
                }
            ).apply {
                disposable.add(this)
            }
    }
    private fun insertPopSongsToDatabase(songs: List<Song>) {
        songs.forEach{
            it.genre = GENRE
            removeEmptyFields(it)
        }

        databaseRepository.insertAll(songs)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { getPopSongsFromDatabase()}
            ).apply {
                disposable.add(this)
            }
    }
    private fun getPopSongsFromDatabase() {
        databaseRepository.getAllByGenre(GENRE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { songs ->
                    songViewContract?.songSuccess(songs) },
                { error ->
                    songViewContract?.songFailed(error) }
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
        const val GENRE = "pop"
    }
}

interface SongPresenterPopContract {
    fun initializePresenter(viewContract: SongViewContract)
    fun getPopSongs()
    fun destroy()
    fun checkNetwork()
}
