package com.example.film.ui.film.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.film.BuildConfig
import com.example.film.api.ApiConfig
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.local.MovieEntity
import com.example.film.data.resources.remote.response.DetailMovieResponse
import com.example.film.utils.MockResponseFileReader
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class DetailFilmViewModelTest {
    private lateinit var viewModel: DetailFilmViewModel
    private val movieId = 460465

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var filmRepository: FilmRepository

    private lateinit var mockWebServer : MockWebServer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = DetailFilmViewModel(filmRepository)

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Test
    fun readSuccessJSON(){
        val reader = MockResponseFileReader("detail_movie.json")
        assertNotNull(reader.content)
    }

    @Test
    fun checkSuccessResponse(){

        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("detail_movie.json").content)
        mockWebServer.enqueue(response)

        val  apiResponse = ApiConfig.getApiService().getDetailMovie(movieId, BuildConfig.API_KEY).execute()

        assertEquals(response.toString().contains("200"), apiResponse.code().toString().contains("200"))
    }

    @Test
    fun testSetSelectedMovie(){
        viewModel.setSelectedMovies(movieId)

        val viewModel = viewModel.setSelectedMovies(movieId)
        assertNotNull(viewModel)
        assertEquals(viewModel, movieId)
    }

    @Test
    fun getDetailMovies(){
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("detail_movie.json").content)
        mockWebServer.enqueue(response)
        val mockResponse = response.getBody()?.readUtf8()

        val liveDetailMovie = MutableLiveData<DetailMovieResponse>()
        val movie = ApiConfig.getApiService().getDetailMovie(movieId, BuildConfig.API_KEY).execute().body()
        liveDetailMovie.value = movie
        `when`(filmRepository.getDetailMovie(movieId)).thenReturn(liveDetailMovie)
        viewModel.setSelectedMovies(movieId)
        val viewModelResponse = viewModel.getDetailMovies().value
        verify(filmRepository).getDetailMovie(movieId)

        assertEquals(mockResponse?.let { parseMockedJSON(it) }, viewModelResponse?.title)
    }

    private fun parseMockedJSON(mockResponse: String): String {
        val reader = JSONObject(mockResponse)
        return reader.getString("title")
    }

    @Test
    fun testGetFavoriteMovie(){
        val movie = ApiConfig.getApiService().getDetailMovie(movieId, BuildConfig.API_KEY).execute().body()
        val liveDetailMovie = MutableLiveData<MovieEntity>()
        val movieEntity = MovieEntity(
            movie!!.title,
            movie.posterPath,
            movie.releaseDate,
            movie.voteAverage,
            movie.id
        )
        liveDetailMovie.value = movieEntity
        `when`(filmRepository.getFavoriteMovieById(movieId)).thenReturn(liveDetailMovie)
        viewModel.setSelectedMovies(movieId)
        val viewModelResult = viewModel.getFavoriteMovie()
        verify(filmRepository, times(1)).getFavoriteMovieById(movieId)
        assertNotNull(viewModelResult)
        assertEquals(viewModelResult.value, liveDetailMovie.value)
    }

    @Test
    fun testSetFavorite(){
        val movie = ApiConfig.getApiService().getDetailMovie(movieId, BuildConfig.API_KEY).execute().body()
        val movieEntity = MovieEntity(
            movie!!.title,
            movie.posterPath,
            movie.releaseDate,
            movie.voteAverage,
            movie.id
        )
        doNothing().`when`(filmRepository).insertFavoriteMovie(movieEntity)
        viewModel.setFavorite(movie!!)
        verify(filmRepository, times(1)).insertFavoriteMovie(movieEntity)
    }

    @Test
    fun testDeleteFavorite(){
        val movie = ApiConfig.getApiService().getDetailMovie(movieId, BuildConfig.API_KEY).execute().body()
        val movieEntity = MovieEntity(
            movie!!.title,
            movie.posterPath,
            movie.releaseDate,
            movie.voteAverage,
            movie.id
        )
        doNothing().`when`(filmRepository).deleteFavoriteMovie(movieEntity)
        viewModel.deleteFavorite(movie!!)
        verify(filmRepository, times(1)).deleteFavoriteMovie(movieEntity)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}