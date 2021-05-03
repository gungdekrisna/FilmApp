package com.example.film.ui.tv_show

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.film.BuildConfig
import com.example.film.api.ApiConfig
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.remote.response.TvResultsItem
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
class TvShowViewModelTest {
    private lateinit var viewModel: TvShowViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var filmRepository: FilmRepository

    private lateinit var mockWebServer : MockWebServer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = TvShowViewModel(filmRepository)

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Test
    fun readSuccessJSON(){
        val reader = MockResponseFileReader("tvs.json")
        assertNotNull(reader.content)
    }

    @Test
    fun checkSuccessResponse(){

        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("tvs.json").content)
        mockWebServer.enqueue(response)

        val  apiResponse = ApiConfig.getApiService().getPopularTvs(BuildConfig.API_KEY, 1).execute()

        assertEquals(response.toString().contains("200"), apiResponse.code().toString().contains("200"))
    }

    @Test
    fun getTvShows(){

        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("tvs.json").content)
        mockWebServer.enqueue(response)
        val mockResponse = response.getBody()?.readUtf8()

        val listTvs = MutableLiveData<List<TvResultsItem>>()
        val tv = ApiConfig.getApiService().getPopularTvs(BuildConfig.API_KEY, 1).execute().body()?.results
        listTvs.value = tv
        `when`(filmRepository.getTvs()).thenReturn(listTvs)
        val viewModelResponse = viewModel.getTvShows().value
        verify(filmRepository).getTvs()

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