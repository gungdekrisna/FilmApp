package com.example.film.data.resources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.film.data.resources.remote.RemoteDataSource
import com.example.film.data.resources.remote.response.DetailMovieResponse
import com.example.film.data.resources.remote.response.DetailTvResponse
import com.example.film.data.resources.remote.response.TvResultsItem
import com.example.film.resources.remote.response.MovieResultsItem

class FilmRepository private constructor(private val remoteDataSource: RemoteDataSource) : FilmDataSource {
    companion object {
        @Volatile
        private var instance: FilmRepository? = null
        fun getInstance(remoteData: RemoteDataSource): FilmRepository =
                instance ?: synchronized(this) {
                    instance ?: FilmRepository(remoteData).apply { instance = this }
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
}