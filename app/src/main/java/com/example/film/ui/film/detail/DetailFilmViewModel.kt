package com.example.film.ui.film.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.remote.response.DetailMovieResponse

class DetailFilmViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    private var filmId: Int = 0

    fun setSelectedMovies(filmId: Int) {
        this.filmId = filmId
    }

    fun getDetailMovies() : LiveData<DetailMovieResponse> = filmRepository.getDetailMovie(filmId)
}