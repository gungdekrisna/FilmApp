package com.example.film.data.resources

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.film.data.resources.local.MovieEntity
import com.example.film.data.resources.local.TvShowEntity
import com.example.film.data.resources.remote.response.DetailMovieResponse
import com.example.film.data.resources.remote.response.DetailTvResponse
import com.example.film.data.resources.remote.response.MovieResultsItem
import com.example.film.data.resources.remote.response.TvResultsItem

interface FilmDataSource {
    fun getMovies(): LiveData<List<MovieResultsItem>>
    fun getTvs(): LiveData<List<TvResultsItem>>
    fun getDetailMovie(movieId: Int): LiveData<DetailMovieResponse>
    fun getDetailTv(tvId: Int): LiveData<DetailTvResponse>

    // Favorite
    // Movies
    fun getFavoriteMovies(sort: String): DataSource.Factory<Int, MovieEntity>
    fun getFavoriteMovieById(movieId: Int): LiveData<MovieEntity?>
    fun insertFavoriteMovie(movie : MovieEntity)
    fun deleteFavoriteMovie(movie: MovieEntity)

    // Tv Shows
    fun getFavoriteTvShows(sort: String): DataSource.Factory<Int, TvShowEntity>
    fun getFavoriteTvShowById(tvShowId: Int): LiveData<TvShowEntity?>
    fun insertFavoriteTvShow(tvShow : TvShowEntity)
    fun deleteFavoriteTvShow(tvShow: TvShowEntity)
}