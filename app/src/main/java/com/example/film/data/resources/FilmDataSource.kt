package com.example.film.data.resources

import androidx.lifecycle.LiveData
import com.example.film.data.resources.remote.response.DetailMovieResponse
import com.example.film.data.resources.remote.response.DetailTvResponse
import com.example.film.data.resources.remote.response.TvResultsItem
import com.example.film.resources.remote.response.MovieResultsItem

interface FilmDataSource {
    fun getMovies(): LiveData<List<MovieResultsItem>>
    fun getTvs(): LiveData<List<TvResultsItem>>
    fun getDetailMovie(movieId: Int): LiveData<DetailMovieResponse>
    fun getDetailTv(tvId: Int): LiveData<DetailTvResponse>
}