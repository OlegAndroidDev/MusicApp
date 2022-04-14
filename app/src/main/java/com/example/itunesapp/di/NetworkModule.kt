package com.example.itunesapp.di

import com.example.itunesapp.restapi.ItunesApi
import com.example.itunesapp.restapi.RequestInterceptor
import com.example.itunesapp.restapi.SongRepository
import com.example.itunesapp.restapi.SongRepositoryImpl
import com.google.gson.Gson
import dagger.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    fun providesGson() = Gson()

    @Provides
    fun providesLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun providesRequestInterceptor() = RequestInterceptor()

    @Provides
    fun providesOkHttpClient(
        requestInterceptor: RequestInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun providesRetrofitService(okHttpClient: OkHttpClient, gson: Gson) : ItunesApi {
        return Retrofit.Builder()
            .baseUrl(ItunesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ItunesApi::class.java)
    }
    @Provides
    fun providesSongRepository(songApi: ItunesApi) : SongRepository {
        return SongRepositoryImpl(songApi)
    }
}