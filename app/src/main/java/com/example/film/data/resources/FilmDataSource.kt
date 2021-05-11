package com.example.film.data.resources

import androidx.lifecycle.LiveData
import com.example.film.data.resources.local.MovieEntity
import com.example.film.data.resources.remote.response.DetailMovieResponse
import com.example.film.data.resources.remote.response.DetailTvResponse
import com.example.film.data.resources.remote.response.MovieResultsItem
import com.example.film.data.resources.remote.response.TvResultsItem

interface FilmDataSource {
    fun getMovies(): LiveData<List<MovieResultsItem>>
    fun getTvs(): LiveData<List<TvResultsItem>>
    fun getDetailMovie(movieId: Int): LiveData<DetailMovieResponse>
    fun getDetailTv(tvId: Int): LiveData<DetailTvResponse>
    fun getFavoriteMovies(): LiveData<List<MovieEntity>>
    fun getFavoriteMovieById(movieId: Int): LiveData<MovieEntity?>
    fun insertFavoriteMovie(movie : MovieEntity)
    fun deleteFavoriteMovie(movie: MovieEntity)
}