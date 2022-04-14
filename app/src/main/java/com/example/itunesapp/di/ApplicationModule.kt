package com.example.itunesapp.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.room.Room
import com.example.itunesapp.database.DatabaseRepository
import com.example.itunesapp.database.DatabaseRepositoryImpl
import com.example.itunesapp.database.SongDao
import com.example.itunesapp.database.SongDatabase
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(
    private val applicationContext: Context
){
    @Provides
    fun providesApplicationContext() = applicationContext

    @Provides
    fun providesConnectivityManager(context: Context) : ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    fun providesNetworkRequest() : NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
    }

    @Provides
    fun providesDatabase(context: Context) : SongDatabase {
        return Room.databaseBuilder(
            context,
            SongDatabase::class.java, "song_database"
        ).build()
    }

    @Provides
    fun providesDao(database: SongDatabase) : SongDao = database.songDao()

    @Provides
    fun providesDatabaseRepository(dao: SongDao) : DatabaseRepository =
        DatabaseRepositoryImpl(dao)
}