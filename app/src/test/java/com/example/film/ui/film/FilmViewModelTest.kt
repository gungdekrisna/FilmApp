package com.example.film.ui.film

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.film.BuildConfig
import com.example.film.api.ApiConfig
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.remote.response.MovieResultsItem
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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class FilmViewModelTest {

    private lateinit var viewModel: FilmViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var filmRepository: FilmRepository

    private lateinit var mockWebServer : MockWebServer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = FilmViewModel(filmRepository)

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Test
    fun readSuccessJSON(){
        val reader = MockResponseFileReader("movies.json")
        assertNotNull(reader.content)
    }

    @Test
    fun checkSuccessResponse(){

        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("movies.json").content)
        mockWebServer.enqueue(response)

        val  apiResponse = ApiConfig.getApiService().getPopularMovies(BuildConfig.API_KEY, 1).execute()

        assertEquals(response.toString().contains("200"), apiResponse.code().toString().contains("200"))
    }

    @Test
    fun getMovies(){

        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("movies.json").content)
        mockWebServer.enqueue(response)
        val mockResponse = response.getBody()?.readUtf8()

        val liveMovies = MutableLiveData<List<MovieResultsItem>>()
        val movies = ApiConfig.getApiService().getPopularMovies(BuildConfig.API_KEY, 1).execute().body()?.results
        liveMovies.value = movies
        `when`(filmRepository.getMovies()).thenReturn(liveMovies)
        val viewModelResponse = viewModel.getMovies().value
        verify(filmRepository).getMovies()

        assertEquals(mockResponse?.let { parseMockedJSON(it) }, viewModelResponse?.size)
    }

    private fun parseMockedJSON(mockResponse: String): Int {
        val reader = JSONObject(mockResponse)
        return reader.getJSONArray("results").length()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}