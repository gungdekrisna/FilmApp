package com.example.film.ui.tv_show.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.film.BuildConfig
import com.example.film.api.ApiConfig
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.remote.response.DetailTvResponse
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
class DetailTvShowViewModelTest {
    private lateinit var viewModel: DetailTvShowViewModel
    private val tvId = 88396

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var filmRepository: FilmRepository

    private lateinit var mockWebServer : MockWebServer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = DetailTvShowViewModel(filmRepository)
        viewModel.setSelectedTvShow(tvId)

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Test
    fun readSuccessJSON(){
        val reader = MockResponseFileReader("detail_tv.json")
        assertNotNull(reader.content)
    }

    @Test
    fun checkSuccessResponse(){

        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("detail_tv.json").content)
        mockWebServer.enqueue(response)

        val  apiResponse = ApiConfig.getApiService().getDetailTv(tvId, BuildConfig.API_KEY).execute()

        assertEquals(response.toString().contains("200"), apiResponse.code().toString().contains("200"))
    }

    @Test
    fun getDetailTv(){
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockResponseFileReader("detail_tv.json").content)
        mockWebServer.enqueue(response)
        val mockResponse = response.getBody()?.readUtf8()

        val liveDetailTv = MutableLiveData<DetailTvResponse>()
        val tv = ApiConfig.getApiService().getDetailTv(tvId, BuildConfig.API_KEY).execute().body()
        liveDetailTv.value = tv
        `when`(filmRepository.getDetailTv(tvId)).thenReturn(liveDetailTv)
        val viewModelResponse = viewModel.getDetailTv().value
        verify(filmRepository).getDetailTv(tvId)

        assertEquals(mockResponse?.let { parseMockedJSON(it) }, viewModelResponse?.name)
    }

    private fun parseMockedJSON(mockResponse: String): String {
        val reader = JSONObject(mockResponse)
        return reader.getString("name")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}