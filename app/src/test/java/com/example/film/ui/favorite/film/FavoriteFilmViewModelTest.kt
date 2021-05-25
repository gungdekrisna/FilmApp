package com.example.film.ui.favorite.film

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.LivePagedListBuilder
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.local.AppDatabase
import com.example.film.data.resources.local.FilmDao
import com.example.film.utils.SortUtils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoriteFilmViewModelTest {

    private lateinit var viewModel: FavoriteFilmViewModel
    private lateinit var filmDao: FilmDao
    private lateinit var db: AppDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var filmRepository: FilmRepository

    @Before
    fun setUp() {
        val context = mock(Context::class.java)
        db = AppDatabase.getDatabase(context)
        filmDao = db.filmDao()
        viewModel = FavoriteFilmViewModel(filmRepository)
    }

    @Test
    fun getFavoriteMovie(){
        val query = SortUtils.getSortedMovies(SortUtils.HIGHEST)
        val favoriteMoviesDataSource = filmDao.getFavoriteMovies(query)
        val movieLiveData = LivePagedListBuilder(favoriteMoviesDataSource, 20).build()

        `when`(filmRepository.getFavoriteMovies(SortUtils.HIGHEST)).thenReturn(favoriteMoviesDataSource)

        val viewModelResult = viewModel.getFavoriteMovie(SortUtils.HIGHEST)
        verify(filmRepository).getFavoriteMovies(SortUtils.HIGHEST)

        assertNotNull(viewModelResult)
        assertEquals(viewModelResult.value, movieLiveData.value)
    }
}