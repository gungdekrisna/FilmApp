package com.example.film.ui.favorite.tv_show

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.LivePagedListBuilder
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.local.AppDatabase
import com.example.film.data.resources.local.FilmDao
import com.example.film.utils.SortUtils
import junit.framework.Assert.assertNotNull
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoriteTvShowViewModelTest {
    private lateinit var viewModel: FavoriteTvShowViewModel
    private lateinit var filmDao: FilmDao
    private lateinit var db: AppDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var filmRepository: FilmRepository

    @Before
    fun setUp() {
        val context = Mockito.mock(Context::class.java)
        db = AppDatabase.getDatabase(context)
        filmDao = db.filmDao()
        viewModel = FavoriteTvShowViewModel(filmRepository)
    }

    @Test
    fun testGetFavoriteTvShow(){
        val query = SortUtils.getSortedTvShows(SortUtils.HIGHEST)
        val favoriteTvShowsDataSource = filmDao.getFavoriteTvShows(query)
        val tvShowLiveData = LivePagedListBuilder(favoriteTvShowsDataSource, 20).build()

        `when`(filmRepository.getFavoriteTvShows(SortUtils.HIGHEST)).thenReturn(favoriteTvShowsDataSource)

        val viewModelResult = viewModel.getFavoriteTvShow(SortUtils.HIGHEST)
        verify(filmRepository).getFavoriteTvShows(SortUtils.HIGHEST)

        assertNotNull(viewModelResult)
        assertEquals(viewModelResult.value, tvShowLiveData.value)
    }
}