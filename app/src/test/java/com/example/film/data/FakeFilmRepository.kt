package com.example.film.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.film.data.resources.FilmDataSource
import com.example.film.data.resources.local.LocalDataSource
import com.example.film.data.resources.local.MovieEntity
import com.example.film.data.resources.local.TvShowEntity
import com.example.film.data.resources.remote.RemoteDataSource
import com.example.film.data.resources.remote.response.DetailMovieResponse
import com.example.film.data.resources.remote.response.DetailTvResponse
import com.example.film.data.resources.remote.response.MovieResultsItem
import com.example.film.data.resources.remote.response.TvResultsItem
import com.example.film.utils.AppExecutors

class FakeFilmRepository constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
    ) : FilmDataSource {

    override fun getMovies(): LiveData<List<MovieResultsItem>> {
        val movieResults = MutableLiveData<List<MovieResultsItem>>()
        remoteDataSource.getMovies(object : RemoteDataSource.LoadMoviesCallback {
            override fun onMoviesReceived(movieResponses: List<MovieResultsItem>) {
                movieResults.postValue(movieResponses)
            }
        })

        return movieResults
    }

    override fun getTvs(): LiveData<List<TvResultsItem>> {
        val tvResults = MutableLiveData<List<TvResultsItem>>()
        remoteDataSource.getTvs(object : RemoteDataSource.LoadTvsCallback {
            override fun onTvsReceived(tvResponses: List<TvResultsItem>) {
                tvResults.postValue(tvResponses)
            }
        })

        return tvResults
    }

    override fun getDetailMovie(movieId: Int): LiveData<DetailMovieResponse> {
        val detailMovieResult = MutableLiveData<DetailMovieResponse>()
        remoteDataSource.getDetailMovie(movieId, object : RemoteDataSource.LoadDetailMovieCallback {
            override fun onDetailMovieReceived(detailMovieResponse: DetailMovieResponse) {
                detailMovieResult.postValue(detailMovieResponse)
            }
        })

        return detailMovieResult
    }

    override fun getDetailTv(tvId: Int): LiveData<DetailTvResponse> {
        val detailTvResult = MutableLiveData<DetailTvResponse>()
        remoteDataSource.getDetailTv(tvId, object : RemoteDataSource.LoadDetailTvCallback {
            override fun onDetailTvReceived(detailTvResponse: DetailTvResponse) {
                detailTvResult.postValue(detailTvResponse)
            }
        })

        return detailTvResult
    }

    // Favorite
    // Movies
    override fun getFavoriteMovies(sort: String): DataSource.Factory<Int, MovieEntity> {
        var favoriteMovieResult : DataSource.Factory<Int, MovieEntity>? = null

        localDataSource.getFavoriteMovies(sort, object : LocalDataSource.LoadFavoriteMoviesCallback {
            override fun onFavoriteMoviesReceived(movieEntity: DataSource.Factory<Int, MovieEntity>) {
                favoriteMovieResult = movieEntity
            }
        })
        return favoriteMovieResult!!
    }

    override fun getFavoriteMovieById(movieId: Int): LiveData<MovieEntity?> {
        val favoriteMovieByIdResult = MutableLiveData<MovieEntity?>()
        appExecutors.diskIO().execute {
            localDataSource.getFavoriteMovieById(movieId, object : LocalDataSource.LoadFavoriteMovieByIdCallback {
                override fun onFavoriteMovieByIdReceived(movieEntity: MovieEntity?) {
                    favoriteMovieByIdResult.postValue(movieEntity)
                }
            })
        }
        return favoriteMovieByIdResult
    }

    override fun insertFavoriteMovie(movie: MovieEntity) {
        appExecutors.diskIO().execute { localDataSource.insertFavoriteMovie(movie) }
    }

    override fun deleteFavoriteMovie(movie: MovieEntity) {
        appExecutors.diskIO().execute { localDataSource.deleteFavoriteMovie(movie) }
    }

    // Tv Shows
    override fun getFavoriteTvShows(sort: String): DataSource.Factory<Int, TvShowEntity> {
        var favoriteTvShowResult : DataSource.Factory<Int, TvShowEntity>? = null

        localDataSource.getFavoriteTvShows(sort, object : LocalDataSource.LoadFavoriteTvShowsCallback {
            override fun onFavoriteTvShowsReceived(tvShowEntity: DataSource.Factory<Int, TvShowEntity>) {
                favoriteTvShowResult = tvShowEntity
            }
        })
        return favoriteTvShowResult!!
    }

    override fun getFavoriteTvShowById(tvShowId: Int): LiveData<TvShowEntity?> {
        val favoriteTvShowByIdResult = MutableLiveData<TvShowEntity?>()

        appExecutors.diskIO().execute {
            localDataSource.getFavoriteTvShowById(tvShowId, object : LocalDataSource.LoadFavoriteTvShowByIdCallback {
                override fun onFavoriteTvShowByIdReceived(tvShowEntity: TvShowEntity?) {
                    favoriteTvShowByIdResult.postValue(tvShowEntity)
                }
            })
        }
        return favoriteTvShowByIdResult
    }

    override fun insertFavoriteTvShow(tvShow: TvShowEntity) {
        appExecutors.diskIO().execute { localDataSource.insertFavoriteTvShow(tvShow) }
    }

    override fun deleteFavoriteTvShow(tvShow: TvShowEntity) {
        appExecutors.diskIO().execute { localDataSource.deleteFavoriteTvShow(tvShow) }
    }
}