package com.example.film.data.resources.remote

import android.util.Log
import com.example.film.BuildConfig
import com.example.film.api.ApiConfig
import com.example.film.data.resources.remote.response.DetailMovieResponse
import com.example.film.data.resources.remote.response.DetailTvResponse
import com.example.film.data.resources.remote.response.TvResponse
import com.example.film.data.resources.remote.response.TvResultsItem
import com.example.film.resources.remote.response.MovieResponse
import com.example.film.resources.remote.response.MovieResultsItem
import com.example.film.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
                instance ?: synchronized(this) {
                    instance ?: RemoteDataSource().apply { instance = this }
                }

        const val TAG = "RemoteDataSource"
    }

    fun getMovies(callback: LoadMoviesCallback){
        EspressoIdlingResource.increment()
        val client = ApiConfig.getApiService().getPopularMovies(BuildConfig.API_KEY, 1)
        client.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onMoviesReceived(response.body()!!.results)
                    EspressoIdlingResource.decrement()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getTvs(callback: LoadTvsCallback){
        EspressoIdlingResource.increment()
        val client = ApiConfig.getApiService().getPopularTvs(BuildConfig.API_KEY, 1)
        client.enqueue(object : Callback<TvResponse> {
            override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onTvsReceived(response.body()!!.results)
                    EspressoIdlingResource.decrement()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetailMovie(movieId: Int, callback: LoadDetailMovieCallback){
        EspressoIdlingResource.increment()
        val client = ApiConfig.getApiService().getDetailMovie(movieId, BuildConfig.API_KEY)
        client.enqueue(object : Callback<DetailMovieResponse> {
            override fun onResponse(
                    call: Call<DetailMovieResponse>,
                    response: Response<DetailMovieResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onDetailMovieReceived(response.body()!!)
                    EspressoIdlingResource.decrement()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailMovieResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetailTv(tvId: Int, callback: LoadDetailTvCallback){
        EspressoIdlingResource.increment()
        val client = ApiConfig.getApiService().getDetailTv(tvId, BuildConfig.API_KEY)
        client.enqueue(object : Callback<DetailTvResponse> {
            override fun onResponse(
                    call: Call<DetailTvResponse>,
                    response: Response<DetailTvResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onDetailTvReceived(response.body()!!)
                    EspressoIdlingResource.decrement()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailTvResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    interface LoadMoviesCallback {
        fun onMoviesReceived(movieResponses: List<MovieResultsItem>)
    }
    interface LoadTvsCallback {
        fun onTvsReceived(tvResponses: List<TvResultsItem>)
    }
    interface LoadDetailMovieCallback {
        fun onDetailMovieReceived(detailMovieResponse: DetailMovieResponse)
    }
    interface LoadDetailTvCallback {
        fun onDetailTvReceived(detailTvResponse: DetailTvResponse)
    }
}