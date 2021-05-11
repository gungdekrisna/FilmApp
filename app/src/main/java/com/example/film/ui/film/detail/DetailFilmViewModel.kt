package com.example.film.ui.film.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.local.MovieEntity
import com.example.film.data.resources.remote.response.DetailMovieResponse

class DetailFilmViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    private var filmId: Int = 0
    // private val filmId = MutableLiveData<Int>()

    fun setSelectedMovies(filmId: Int) {
        this.filmId = filmId
    }

    /*fun setSelectedMovies(filmId: Int) {
        this.filmId.value = filmId
    }*/

    fun getDetailMovies() : LiveData<DetailMovieResponse> = filmRepository.getDetailMovie(filmId)

    fun getFavoriteMovie() : LiveData<MovieEntity?> {
        return filmRepository.getFavoriteMovieById(filmId)
    }

    fun setFavorite(detailMovieResponse: DetailMovieResponse) {
        val movieEntity = MovieEntity(
            detailMovieResponse.title,
            detailMovieResponse.posterPath,
            detailMovieResponse.releaseDate,
            detailMovieResponse.voteAverage,
            detailMovieResponse.id
        )
        filmRepository.insertFavoriteMovie(movieEntity)
    }

    fun deleteFavorite(detailMovieResponse: DetailMovieResponse) {
        val movieEntity = MovieEntity(
            detailMovieResponse.title,
            detailMovieResponse.posterPath,
            detailMovieResponse.releaseDate,
            detailMovieResponse.voteAverage,
            detailMovieResponse.id
        )
        filmRepository.deleteFavoriteMovie(movieEntity)
    }
}