package com.example.film.api

import com.example.film.data.resources.remote.response.DetailMovieResponse
import com.example.film.data.resources.remote.response.DetailTvResponse
import com.example.film.data.resources.remote.response.TvResponse
import com.example.film.data.resources.remote.response.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<MovieResponse>

    @GET("movie/{id}")
    fun getDetailMovie(@Path("id") movieId: Int, @Query("api_key") apiKey: String): Call<DetailMovieResponse>

    @GET("tv/popular")
    fun getPopularTvs(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<TvResponse>

    @GET("tv/{id}")
    fun getDetailTv(@Path("id") tvId: Int, @Query("api_key") apiKey: String): Call<DetailTvResponse>
}