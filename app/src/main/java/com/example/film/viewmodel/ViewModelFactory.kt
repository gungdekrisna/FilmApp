package com.example.film.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.film.data.resources.FilmRepository
import com.example.film.di.Injection
import com.example.film.ui.favorite.film.FavoriteFilmViewModel
import com.example.film.ui.favorite.tv_show.FavoriteTvShowViewModel
import com.example.film.ui.film.FilmViewModel
import com.example.film.ui.film.detail.DetailFilmViewModel
import com.example.film.ui.tv_show.TvShowViewModel
import com.example.film.ui.tv_show.detail.DetailTvShowViewModel

class ViewModelFactory private constructor(private val filmRepository: FilmRepository) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                    instance = this
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(FilmViewModel::class.java) -> {
                return FilmViewModel(filmRepository) as T
            }
            modelClass.isAssignableFrom(TvShowViewModel::class.java) -> {
                return TvShowViewModel(filmRepository) as T
            }
            modelClass.isAssignableFrom(DetailFilmViewModel::class.java) -> {
                return DetailFilmViewModel(filmRepository) as T
            }
            modelClass.isAssignableFrom(DetailTvShowViewModel::class.java) -> {
                return DetailTvShowViewModel(filmRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteFilmViewModel::class.java) -> {
                return FavoriteFilmViewModel(filmRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteTvShowViewModel::class.java) -> {
                return FavoriteTvShowViewModel(filmRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }
}