package com.example.film.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.film.BuildConfig
import com.example.film.api.ApiConfig
import com.example.film.data.resources.local.*
import com.example.film.data.resources.remote.RemoteDataSource
import com.example.film.utils.AppExecutors
import com.example.film.utils.LiveDataTestUtil
import com.example.film.utils.SortUtils
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class FilmRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)
    private val executor = mock(AppExecutors::class.java)
    private val filmRepository = FakeFilmRepository(remote, local, executor)
    private lateinit var appExecutors: AppExecutors

    private lateinit var filmDao: FilmDao
    private lateinit var db: AppDatabase

    private val tvId = 88396
    private val movieId = 460465

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        filmDao = db.filmDao()
        appExecutors = AppExecutors()
    }

    @After
    fun tearDown() {
        db.close()
    }

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

    @Test
    fun testGetFavoriteMovies() {
        val query = SortUtils.getSortedMovies(SortUtils.HIGHEST)
        val favoriteMoviesDataSource = filmDao.getFavoriteMovies(query)

        doAnswer { invocation ->
            (invocation.arguments[1] as LocalDataSource.LoadFavoriteMoviesCallback)
                .onFavoriteMoviesReceived(favoriteMoviesDataSource)
            null
        }.`when`(local).getFavoriteMovies(eq(SortUtils.HIGHEST), any())

        val favoriteMovies = filmRepository.getFavoriteMovies(SortUtils.HIGHEST)
        verify(local).getFavoriteMovies(eq(SortUtils.HIGHEST), any())

        assertNotNull(favoriteMovies)
        assertEquals(favoriteMoviesDataSource, favoriteMovies)
    }

    @Test
    fun testGetFavoriteMovieById() {
        val movie = MovieEntity(
            "Tom Clancy's Without Remorse",
            "/rEm96ib0sPiZBADNKBHKBv5bve9.jpg",
            "2021-04-29",
            7.3,
            567189
        )

        filmDao.insertFavoriteMovie(movie)
        val favoriteMovieByIdDataSource = filmDao.getFavoriteMovieById(movie.id)

        appExecutors.diskIO().execute {
            doAnswer { invocation ->
                (invocation.arguments[1] as LocalDataSource.LoadFavoriteMovieByIdCallback)
                    .onFavoriteMovieByIdReceived(favoriteMovieByIdDataSource)
                null
            }.`when`(local).getFavoriteMovieById(eq(movie.id), any())

            val favoriteMovieById = filmRepository.getFavoriteMovieById(movie.id)
            verify(local).getFavoriteMovieById(eq(movie.id), any())
            assertNotNull(favoriteMovieById)
            assertEquals(favoriteMovieByIdDataSource, favoriteMovieById.value)
        }
    }

    @Test
    fun testInsertFavoriteMovie() {
        val movie = MovieEntity(
            "Tom Clancy's Without Remorse",
            "/rEm96ib0sPiZBADNKBHKBv5bve9.jpg",
            "2021-04-29",
            7.3,
            567189
        )
        appExecutors.diskIO().execute {
            doNothing().`when`(local).insertFavoriteMovie(movie)
            filmRepository.insertFavoriteMovie(movie)
            verify(local, times(1)).insertFavoriteMovie(movie)
        }
    }

    @Test
    fun testDeleteFavoriteMovie() {
        val movie = MovieEntity(
            "Tom Clancy's Without Remorse",
            "/rEm96ib0sPiZBADNKBHKBv5bve9.jpg",
            "2021-04-29",
            7.3,
            567189
        )
        appExecutors.diskIO().execute {
            doNothing().`when`(local).insertFavoriteMovie(movie)
            filmRepository.insertFavoriteMovie(movie)
            verify(local, times(1)).insertFavoriteMovie(movie)
        }
    }

    @Test
    fun testGetFavoriteTvShows() {
        val query = SortUtils.getSortedTvShows(SortUtils.HIGHEST)
        val favoriteTvShowsDataSource = filmDao.getFavoriteTvShows(query)

        doAnswer { invocation ->
            (invocation.arguments[1] as LocalDataSource.LoadFavoriteTvShowsCallback)
                .onFavoriteTvShowsReceived(favoriteTvShowsDataSource)
            null
        }.`when`(local).getFavoriteTvShows(eq(SortUtils.HIGHEST), any())

        val favoriteTvShows = filmRepository.getFavoriteTvShows(SortUtils.HIGHEST)
        verify(local).getFavoriteTvShows(eq(SortUtils.HIGHEST), any())

        assertNotNull(favoriteTvShows)
        assertEquals(favoriteTvShowsDataSource, favoriteTvShows)
    }

    @Test
    fun testGetFavoriteTvShowById() {
        val tvShow = TvShowEntity(
            "The Falcon and the Winter Soldier",
            "/6kbAMLteGO8yyewYau6bJ683sw7.jpg",
            "2021-03-19",
            7.9,
            88396
        )

        filmDao.insertFavoriteTvShow(tvShow)
        val favoriteTvShowByIdDataSource = filmDao.getFavoriteTvShowById(tvShow.id)

        appExecutors.diskIO().execute {
            doAnswer { invocation ->
                (invocation.arguments[1] as LocalDataSource.LoadFavoriteTvShowByIdCallback)
                    .onFavoriteTvShowByIdReceived(favoriteTvShowByIdDataSource)
                null
            }.`when`(local).getFavoriteTvShowById(eq(tvShow.id), any())

            val favoriteTvShowById = filmRepository.getFavoriteTvShowById(tvShow.id)
            verify(local).getFavoriteTvShowById(eq(tvShow.id), any())
            assertNotNull(favoriteTvShowById)
            assertEquals(favoriteTvShowByIdDataSource, favoriteTvShowById.value)
        }
    }

    @Test
    fun testInsertFavoriteTvShow() {
        val tvShow = TvShowEntity(
            "The Falcon and the Winter Soldier",
            "/6kbAMLteGO8yyewYau6bJ683sw7.jpg",
            "2021-03-19",
            7.9,
            88396
        )
        appExecutors.diskIO().execute {
            doNothing().`when`(local).insertFavoriteTvShow(tvShow)
            filmRepository.insertFavoriteTvShow(tvShow)
            verify(local, times(1)).insertFavoriteTvShow(tvShow)
        }
    }

    @Test
    fun testDeleteFavoriteTvShow() {
        val tvShow = TvShowEntity(
            "The Falcon and the Winter Soldier",
            "/6kbAMLteGO8yyewYau6bJ683sw7.jpg",
            "2021-03-19",
            7.9,
            88396
        )
        appExecutors.diskIO().execute {
            doNothing().`when`(local).deleteFavoriteTvShow(tvShow)
            filmRepository.deleteFavoriteTvShow(tvShow)
            verify(local, times(1)).deleteFavoriteTvShow(tvShow)
        }
    }
}