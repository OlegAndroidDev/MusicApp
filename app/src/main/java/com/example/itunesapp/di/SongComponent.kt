package com.example.itunesapp.di

import com.example.itunesapp.MainActivity
import com.example.itunesapp.views.ClassicFragment
import com.example.itunesapp.views.PopFragment
import com.example.itunesapp.views.RockFragment
import dagger.Component

@Component (
    modules = [
        NetworkModule::class,
        ApplicationModule::class,
        SongPresenterModule::class
    ]
)

interface SongComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(classicFragment: ClassicFragment)
    fun inject(popFragment: PopFragment)
    fun inject(rockFragment: RockFragment)
}