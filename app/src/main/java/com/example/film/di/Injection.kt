package com.example.film.di

import android.content.Context
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.remote.RemoteDataSource

object Injection {
    fun provideRepository(context: Context): FilmRepository {

        val remoteDataSource = RemoteDataSource.getInstance()

        return FilmRepository.getInstance(remoteDataSource)
    }
}