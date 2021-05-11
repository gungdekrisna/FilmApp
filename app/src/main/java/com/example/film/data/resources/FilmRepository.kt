package com.example.film.data.resources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.film.data.resources.local.LocalDataSource
import com.example.film.data.resources.local.MovieEntity
import com.example.film.data.resources.remote.RemoteDataSource
import com.example.film.data.resources.remote.response.DetailMovieResponse
import com.example.film.data.resources.remote.response.DetailTvResponse
import com.example.film.data.resources.remote.response.MovieResultsItem
import com.example.film.data.resources.remote.response.TvResultsItem
import com.example.film.utils.AppExecutors

class FilmRepository constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
    ) : FilmDataSource {

    companion object {
        @Volatile
        private var instance: FilmRepository? = null

        fun getInstance(remoteData: RemoteDataSource, localData: LocalDataSource, appExecutors: AppExecutors): FilmRepository =
            instance ?: synchronized(this) {
                instance ?: FilmRepository(remoteData, localData, appExecutors).apply {
                    instance = this
                }
            }
    }

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

    override fun getFavoriteMovies(): LiveData<List<MovieEntity>> {
        val favoriteMovieResult = MutableLiveData<List<MovieEntity>>()

        appExecutors.diskIO().execute {
            localDataSource.getFavoriteMovies(object : LocalDataSource.LoadFavoriteMoviesCallback {
                override fun onFavoriteMoviesReceived(movieEntity: List<MovieEntity>) {
                    favoriteMovieResult.postValue(movieEntity)
                }
            })
        }
        return favoriteMovieResult
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
}