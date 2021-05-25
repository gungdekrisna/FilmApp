package com.example.film.ui.home

import android.content.Intent
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.example.film.BuildConfig
import com.example.film.R
import com.example.film.api.ApiConfig
import com.example.film.data.resources.local.AppDatabase
import com.example.film.data.resources.local.FilmDao
import com.example.film.utils.EspressoIdlingResource
import com.example.film.utils.SortUtils
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test


class MainActivityTest {
    private val dataMovies = ApiConfig.getApiService().getPopularMovies(BuildConfig.API_KEY, 1).execute().body()?.results
    private val dataTvShows = ApiConfig.getApiService().getPopularTvs(BuildConfig.API_KEY, 1).execute().body()?.results
    private val movieId = dataMovies!![0].id
    private val tvId = dataTvShows!![0].id
    private val detailMovie = ApiConfig.getApiService().getDetailMovie(movieId, BuildConfig.API_KEY).execute().body()
    private val detailTvShow = ApiConfig.getApiService().getDetailTv(tvId, BuildConfig.API_KEY).execute().body()
    private lateinit var filmDao: FilmDao
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = AppDatabase.getDatabase(context)
        filmDao = db.filmDao()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun loadMovies() {
        onView(withId(R.id.rv_film))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_film))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(dataMovies!!.size))
    }

    @Test
    fun loadDetailMovies() {
        val productionCompanies = ArrayList<String>()
        val genres = ArrayList<String>()

        onView(withId(R.id.rv_film)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        click()
                )
        )
        onView(withId(R.id.tv_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_title)).check(ViewAssertions.matches(withText(detailMovie!!.title)))

        for (genre in detailMovie!!.genres){
            genres.add(genre.name)
        }

        onView(withId(R.id.tv_info)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_info)).check(ViewAssertions.matches(withText("${detailMovie.releaseDate} • ${genres.toString().replace("[", "").replace("]", "")} • ${detailMovie.runtime}m")))

        onView(withId(R.id.tv_overview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_overview)).check(ViewAssertions.matches(withText(detailMovie.overview)))

        onView(withId(R.id.iv_poster)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        for (productionCompany in detailMovie.productionCompanies){
            productionCompanies.add(productionCompany.name)
        }

        onView(withId(R.id.tv_production)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_production)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(withText(productionCompanies.toString().replace("[", "").replace("]", ""))))

        onView(withId(R.id.tv_user_score)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_user_score)).check(ViewAssertions.matches(withText(detailMovie.voteAverage.toString())))
    }

    @Test
    fun shareMovie() {
        val expectedFilmIntent = allOf(
                hasAction(Intent.ACTION_SEND),
                hasExtra(Intent.EXTRA_TEXT, "Watch ${detailMovie!!.title} on new Film App!"),
                IntentMatchers.hasType("text/plain")
        )
        onView(withId(R.id.rv_film)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        click()
                )
        )
        Intents.init()
        onView(withId(R.id.btn_share)).perform(click())
        intended(chooser(expectedFilmIntent))
        Intents.release()
    }

    @Test
    fun loadTvShows() {
        onView(withText("TV Shows")).perform(click())
        onView(withId(R.id.rv_tv_show))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_tv_show)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                        dataTvShows!!.size
                )
        )
    }

    @Test
    fun loadDetailTvShow() {
        val creators = ArrayList<String>()
        val genres = ArrayList<String>()

        onView(withText("TV Shows")).perform(click())
        onView(withId(R.id.rv_tv_show)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        click()
                )
        )
        onView(withId(R.id.tv_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_title)).check(ViewAssertions.matches(withText(detailTvShow!!.name)))

        for (genre in detailTvShow!!.genres){
            genres.add(genre.name)
        }

        onView(withId(R.id.tv_info)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_info)).check(ViewAssertions.matches(withText("${detailTvShow.firstAirDate} • ${genres.toString().replace("[", "").replace("]", "")} • ${detailTvShow.episodeRunTime[0]}m")))

        onView(withId(R.id.tv_overview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_overview)).check(ViewAssertions.matches(withText(detailTvShow.overview)))

        onView(withId(R.id.iv_poster)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        for (creator in detailTvShow.createdBy){
            creators.add(creator.name)
        }

        onView(withId(R.id.tv_creator)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_creator)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(withText(creators.toString().replace("[", "").replace("]", ""))))

        onView(withId(R.id.tv_user_score)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_user_score)).check(ViewAssertions.matches(withText(detailTvShow.voteAverage.toString())))
    }

    @Test
    fun shareTvShow() {
        val expectedTvShowIntent = allOf(
                hasAction(Intent.ACTION_SEND),
                hasExtra(Intent.EXTRA_TEXT, "Watch ${detailTvShow!!.name} on new Film App!"),
                IntentMatchers.hasType("text/plain")
        )
        onView(withText("TV Shows")).perform(click())
        onView(withId(R.id.rv_tv_show)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        click()
                )
        )
        Intents.init()
        onView(withId(R.id.btn_share)).perform(click())
        intended(chooser(expectedTvShowIntent))
        Intents.release()
    }

    @Test
    fun loadFavoriteMovie() {
        val query = SortUtils.getSortedMovies(SortUtils.HIGHEST)
        val favoriteMovie = filmDao.getFavoriteMovies(query)
        val pagedList = LivePagedListBuilder(favoriteMovie, 20).build()

        onView(withId(R.id.rv_film)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                5,
                click()
            )
        )
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withContentDescription("Navigate up")).perform(click())
        onView(withId(R.id.action_favorite)).perform(click())

        onView(withId(R.id.rv_film))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_film))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(pagedList.value!!.size))
    }

    @Test
    fun deleteInsertFavoriteMovie() {
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withId(R.id.rv_film))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_film)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.tv_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_info)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_overview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.iv_poster)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_production)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_user_score)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withId(R.id.action_favorite)).perform(click())
    }

    @Test
    fun loadFavoriteMovieLowest() {
        val query = SortUtils.getSortedMovies(SortUtils.LOWEST)
        val favoriteMovie = filmDao.getFavoriteMovies(query)
        val pagedList = LivePagedListBuilder(favoriteMovie, 20).build()

        onView(withId(R.id.rv_film)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                4,
                click()
            )
        )
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withContentDescription("Navigate up")).perform(click())
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withContentDescription("More options")).perform(click())
        onView(withText(R.string.menu_lowest)).perform(click())
        onView(withId(R.id.rv_film))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_film))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(pagedList.value!!.size))
    }

    @Test
    fun loadFavoriteMovieRandom() {
        val query = SortUtils.getSortedMovies(SortUtils.RANDOM)
        val favoriteMovie = filmDao.getFavoriteMovies(query)
        val pagedList = LivePagedListBuilder(favoriteMovie, 20).build()

        onView(withId(R.id.rv_film)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                3,
                click()
            )
        )
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withContentDescription("Navigate up")).perform(click())
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withContentDescription("More options")).perform(click())
        onView(withText(R.string.menu_random)).perform(click())
        onView(withId(R.id.rv_film))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_film))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(pagedList.value!!.size))
    }

    @Test
    fun loadFavoriteTvShow() {
        val query = SortUtils.getSortedTvShows(SortUtils.HIGHEST)
        val favoriteTvShow = filmDao.getFavoriteTvShows(query)
        val pagedList = LivePagedListBuilder(favoriteTvShow, 20).build()

        onView(withText("TV Shows")).perform(click())
        onView(withId(R.id.rv_tv_show)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                5,
                click()
            )
        )
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withContentDescription("Navigate up")).perform(click())
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withText("TV Shows")).perform(click())
        onView(withId(R.id.rv_tv_show))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_tv_show))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(pagedList.value!!.size))
    }

    @Test
    fun deleteInsertFavoriteTvShow() {
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withText("TV Shows")).perform(click())
        onView(withId(R.id.rv_tv_show))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_tv_show)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.tv_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_info)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_overview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.iv_poster)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_creator)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_user_score)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withId(R.id.action_favorite)).perform(click())
    }

    @Test
    fun loadFavoriteTvShowLowest() {
        val query = SortUtils.getSortedTvShows(SortUtils.LOWEST)
        val favoriteTvShow = filmDao.getFavoriteTvShows(query)
        val pagedList = LivePagedListBuilder(favoriteTvShow, 20).build()

        onView(withText("TV Shows")).perform(click())
        onView(withId(R.id.rv_tv_show)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                4,
                click()
            )
        )
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withContentDescription("Navigate up")).perform(click())
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withText("TV Shows")).perform(click())
        onView(withContentDescription("More options")).perform(click())
        onView(withText(R.string.menu_lowest)).perform(click())
        onView(withId(R.id.rv_tv_show))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_tv_show))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(pagedList.value!!.size))
    }

    @Test
    fun loadFavoriteTvShowRandom() {
        val query = SortUtils.getSortedTvShows(SortUtils.RANDOM)
        val favoriteTvShow = filmDao.getFavoriteTvShows(query)
        val pagedList = LivePagedListBuilder(favoriteTvShow, 20).build()

        onView(withText("TV Shows")).perform(click())
        onView(withId(R.id.rv_tv_show)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                3,
                click()
            )
        )
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withContentDescription("Navigate up")).perform(click())
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withText("TV Shows")).perform(click())
        onView(withContentDescription("More options")).perform(click())
        onView(withText(R.string.menu_random)).perform(click())
        onView(withId(R.id.rv_tv_show))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_tv_show))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(pagedList.value!!.size))
    }

    private fun chooser(matcher: Matcher<Intent>): Matcher<Intent> {
        return allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(`is`(Intent.EXTRA_INTENT), matcher))
    }
}