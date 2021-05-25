package com.example.film.ui.favorite.film

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.local.MovieEntity

class FavoriteFilmViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    fun getFavoriteMovie(sort: String): LiveData<PagedList<MovieEntity>> {
        return LivePagedListBuilder(filmRepository.getFavoriteMovies(sort), 20).build()
    }
}