package com.example.film.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.film.BuildConfig
import com.example.film.api.ApiConfig
import com.example.film.data.resources.remote.RemoteDataSource
import com.example.film.utils.LiveDataTestUtil
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class FilmRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteDataSource::class.java)
    private val filmRepository = FakeFilmRepository(remote)

    private val tvId = 88396
    private val movieId = 460465

    @Test
    fun testGetMovies() {
        val movies = ApiConfig.getApiService().getPopularMovies(BuildConfig.API_KEY, 1).execute().body()?.results

        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadMoviesCallback)
                .onMoviesReceived(movies!!)
            null
        }.`when`(remote).getMovies(any())

        val movieEntities = LiveDataTestUtil.getValue(filmRepository.getMovies())
        verify(remote).getMovies(any())

        assertNotNull(movieEntities)
        assertEquals(movies?.size, movieEntities.size)
    }

    @Test
    fun testGetTvs() {
        val tvs = ApiConfig.getApiService().getPopularTvs(BuildConfig.API_KEY, 1).execute().body()?.results

        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadTvsCallback)
                .onTvsReceived(tvs!!)
            null
        }.`when`(remote).getTvs(any())
        val tvEntities = LiveDataTestUtil.getValue(filmRepository.getTvs())
        verify(remote).getTvs(any())

        assertNotNull(tvEntities)
        assertEquals(tvs?.size, tvEntities.size)
    }

    @Test
    fun testGetDetailMovie() {
        val detailMovie = ApiConfig.getApiService().getDetailMovie(movieId, BuildConfig.API_KEY).execute().body()

        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadDetailMovieCallback)
                .onDetailMovieReceived(detailMovie!!)
            null
        }.`when`(remote).getDetailMovie(eq(movieId), any())
        val detailMovieEntity = LiveDataTestUtil.getValue(filmRepository.getDetailMovie(movieId))
        verify(remote).getDetailMovie(eq(movieId), any())

        assertNotNull(detailMovieEntity)
        assertEquals(detailMovie?.title, detailMovieEntity.title)
    }

    @Test
    fun testGetDetailTv() {
        val detailTv = ApiConfig.getApiService().getDetailTv(tvId, BuildConfig.API_KEY).execute().body()

        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadDetailTvCallback)
                .onDetailTvReceived(detailTv!!)
            null
        }.`when`(remote).getDetailTv(eq(tvId), any())
        val detailTvEntity = LiveDataTestUtil.getValue(filmRepository.getDetailTv(tvId))
        verify(remote).getDetailTv(eq(tvId), any())

        assertNotNull(detailTvEntity)
        assertEquals(detailTv?.name, detailTvEntity.name)
    }
}