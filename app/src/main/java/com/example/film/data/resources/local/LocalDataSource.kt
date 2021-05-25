package com.example.film.data.resources.local

import androidx.paging.DataSource
import com.example.film.utils.EspressoIdlingResource
import com.example.film.utils.SortUtils

class LocalDataSource(private val filmDao: FilmDao){

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(filmDao: FilmDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(filmDao)
    }

    // Movies
    fun getFavoriteMovies(sort: String, callback: LocalDataSource.LoadFavoriteMoviesCallback){
        EspressoIdlingResource.increment()

        val query = SortUtils.getSortedMovies(sort)
        callback.onFavoriteMoviesReceived(filmDao.getFavoriteMovies(query))
        EspressoIdlingResource.decrement()
    }

    fun getFavoriteMovieById(movieId: Int, callback: LocalDataSource.LoadFavoriteMovieByIdCallback){
        EspressoIdlingResource.increment()

        callback.onFavoriteMovieByIdReceived(filmDao.getFavoriteMovieById(movieId))
        EspressoIdlingResource.decrement()
    }

    fun insertFavoriteMovie(movie : MovieEntity){
        EspressoIdlingResource.increment()
        filmDao.insertFavoriteMovie(movie)
        EspressoIdlingResource.decrement()
    }

    fun deleteFavoriteMovie(movie: MovieEntity){
        EspressoIdlingResource.increment()
        filmDao.deleteFavoriteMovie(movie)
        EspressoIdlingResource.decrement()
    }

    // Tv Shows
    fun getFavoriteTvShows(sort: String, callback: LocalDataSource.LoadFavoriteTvShowsCallback){
        EspressoIdlingResource.increment()

        val query = SortUtils.getSortedTvShows(sort)
        callback.onFavoriteTvShowsReceived(filmDao.getFavoriteTvShows(query))
        EspressoIdlingResource.decrement()
    }

    fun getFavoriteTvShowById(tvShowId: Int, callback: LocalDataSource.LoadFavoriteTvShowByIdCallback){
        EspressoIdlingResource.increment()

        callback.onFavoriteTvShowByIdReceived(filmDao.getFavoriteTvShowById(tvShowId))
        EspressoIdlingResource.decrement()
    }

    fun insertFavoriteTvShow(tvShow : TvShowEntity){
        EspressoIdlingResource.increment()
        filmDao.insertFavoriteTvShow(tvShow)
        EspressoIdlingResource.decrement()
    }

    fun deleteFavoriteTvShow(tvShow: TvShowEntity){
        EspressoIdlingResource.increment()
        filmDao.deleteFavoriteTvShow(tvShow)
        EspressoIdlingResource.decrement()
    }

    interface LoadFavoriteMoviesCallback {
        fun onFavoriteMoviesReceived(movieEntity: DataSource.Factory<Int, MovieEntity>)
    }
    interface LoadFavoriteMovieByIdCallback {
        fun onFavoriteMovieByIdReceived(movieEntity: MovieEntity?)
    }
    interface LoadFavoriteTvShowsCallback {
        fun onFavoriteTvShowsReceived(tvShowEntity: DataSource.Factory<Int, TvShowEntity>)
    }
    interface LoadFavoriteTvShowByIdCallback {
        fun onFavoriteTvShowByIdReceived(tvShowEntity: TvShowEntity?)
    }
}