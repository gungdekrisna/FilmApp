package com.example.film.ui.tv_show.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.film.BuildConfig
import com.example.film.api.ApiConfig
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.local.TvShowEntity
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
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class DetailTvShowViewModelTest {
    private lateinit var viewModel: DetailTvShowViewModel
    private val tvId = 95557

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
    fun testSetSelectedTvShow(){
        viewModel.setSelectedTvShow(tvId)

        val viewModel = viewModel.setSelectedTvShow(tvId)
        assertNotNull(viewModel)
        assertEquals(viewModel, tvId)
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

    @Test
    fun testGetFavoriteTvShow(){
        val tv = ApiConfig.getApiService().getDetailTv(tvId, BuildConfig.API_KEY).execute().body()
        val liveDetailTvShow = MutableLiveData<TvShowEntity>()
        val tvShowEntity = TvShowEntity(
            tv!!.name,
            tv.posterPath,
            tv.firstAirDate,
            tv.voteAverage,
            tv.id
        )
        liveDetailTvShow.value = tvShowEntity
        `when`(filmRepository.getFavoriteTvShowById(tvId)).thenReturn(liveDetailTvShow)
        val viewModelResult = viewModel.getFavoriteTvShow()
        verify(filmRepository, times(1)).getFavoriteTvShowById(tvId)
        assertNotNull(viewModelResult)
        assertEquals(viewModelResult.value, liveDetailTvShow.value)
    }

    @Test
    fun testSetFavorite(){
        val tv = ApiConfig.getApiService().getDetailTv(tvId, BuildConfig.API_KEY).execute().body()
        val tvShowEntity = TvShowEntity(
            tv!!.name,
            tv.posterPath,
            tv.firstAirDate,
            tv.voteAverage,
            tv.id
        )
        doNothing().`when`(filmRepository).insertFavoriteTvShow(tvShowEntity)
        viewModel.setFavorite(tv!!)
        verify(filmRepository, times(1)).insertFavoriteTvShow(tvShowEntity)
    }

    @Test
    fun testDeleteFavorite(){
        val tv = ApiConfig.getApiService().getDetailTv(tvId, BuildConfig.API_KEY).execute().body()
        val tvShowEntity = TvShowEntity(
            tv!!.name,
            tv.posterPath,
            tv.firstAirDate,
            tv.voteAverage,
            tv.id
        )
        doNothing().`when`(filmRepository).deleteFavoriteTvShow(tvShowEntity)
        viewModel.deleteFavorite(tv!!)
        verify(filmRepository, times(1)).deleteFavoriteTvShow(tvShowEntity)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}