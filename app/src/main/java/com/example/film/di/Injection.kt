package com.example.film.di

import android.content.Context
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.local.AppDatabase
import com.example.film.data.resources.local.LocalDataSource
import com.example.film.data.resources.remote.RemoteDataSource
import com.example.film.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FilmRepository {

        val database = AppDatabase.getDatabase(context)

        val remoteDataSource = RemoteDataSource.getInstance()
        val localDataSource = LocalDataSource.getInstance(database.filmDao())
        val appExecutors = AppExecutors()

        return FilmRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}