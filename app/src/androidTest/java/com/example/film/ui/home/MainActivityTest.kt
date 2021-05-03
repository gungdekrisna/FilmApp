package com.example.film.ui.home

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.film.BuildConfig
import com.example.film.R
import com.example.film.api.ApiConfig
import com.example.film.utils.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Test


class MainActivityTest {
    private val movieId = 460465
    private val tvId = 88396
    private val dataMovies = ApiConfig.getApiService().getPopularMovies(BuildConfig.API_KEY, 1).execute().body()?.results
    private val dataTvShows = ApiConfig.getApiService().getPopularTvs(BuildConfig.API_KEY, 1).execute().body()?.results
    private val detailMovie = ApiConfig.getApiService().getDetailMovie(movieId, BuildConfig.API_KEY).execute().body()
    private val detailTvShow = ApiConfig.getApiService().getDetailTv(tvId, BuildConfig.API_KEY).execute().body()

    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun loadMovies() {
        if(dataMovies?.size != 0) {
            Espresso.onView(withId(R.id.rv_film))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.rv_film))
                    .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(dataMovies!!.size))
        }
    }

    @Test
    fun loadEmptyMovies() {
        if(dataMovies?.size == 0){
            Espresso.onView(withId(R.id.tv_empty_movie))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.img_empty_icon_film))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun loadDetailMovies() {
        val productionCompanies = ArrayList<String>()
        val genres = ArrayList<String>()

        if(detailMovie != null) {
            Espresso.onView(withId(R.id.rv_film)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            0,
                            ViewActions.click()
                    )
            )
            Espresso.onView(withId(R.id.tv_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.tv_title)).check(ViewAssertions.matches(withText(detailMovie.title)))

            for (genre in detailMovie.genres){
                genres.add(genre.name)
            }

            Espresso.onView(withId(R.id.tv_info)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.tv_info)).check(ViewAssertions.matches(withText("${detailMovie.releaseDate} • ${genres.toString().replace("[", "").replace("]", "")} • ${detailMovie.runtime}m")))

            Espresso.onView(withId(R.id.tv_overview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.tv_overview)).check(ViewAssertions.matches(withText(detailMovie.overview)))

            Espresso.onView(withId(R.id.iv_poster)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

            for (productionCompany in detailMovie.productionCompanies){
                productionCompanies.add(productionCompany.name)
            }

            Espresso.onView(withId(R.id.tv_production)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.tv_production)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(withText(productionCompanies.toString().replace("[", "").replace("]", ""))))

            Espresso.onView(withId(R.id.tv_user_score)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.tv_user_score)).check(ViewAssertions.matches(withText(detailMovie.voteAverage.toString())))
        }
    }

    @Test
    fun shareMovie() {
        if(detailMovie != null) {
            val expectedFilmIntent = Matchers.allOf(
                    IntentMatchers.hasAction(Intent.ACTION_SEND),
                    IntentMatchers.hasExtra(Intent.EXTRA_TEXT, "Watch ${detailMovie.title} on new Film App!"),
                    IntentMatchers.hasType("text/plain")
            )
            Espresso.onView(withId(R.id.rv_film)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            0,
                            ViewActions.click()
                    )
            )
            Intents.init()
            Espresso.onView(withId(R.id.btn_share)).perform(ViewActions.click())
            intended(chooser(expectedFilmIntent))
            Intents.release()
        }
    }

    @Test
    fun loadTvShows() {
        if(dataTvShows?.size != 0) {
            Espresso.onView(ViewMatchers.withText("TV Shows")).perform(ViewActions.click())
            Espresso.onView(withId(R.id.rv_tv_show))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.rv_tv_show)).perform(
                    RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                            dataTvShows!!.size
                    )
            )
        }
    }

    @Test
    fun loadEmptyTvShow() {
        if(dataTvShows?.size == 0){
            Espresso.onView(ViewMatchers.withText("TV Shows")).perform(ViewActions.click())
            Espresso.onView(withId(R.id.tv_empty_tv_show))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.img_empty_icon))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun loadDetailTvShow() {
        val creators = ArrayList<String>()
        val genres = ArrayList<String>()

        if(detailTvShow != null) {
            Espresso.onView(ViewMatchers.withText("TV Shows")).perform(ViewActions.click())
            Espresso.onView(withId(R.id.rv_tv_show)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            0,
                            ViewActions.click()
                    )
            )
            Espresso.onView(withId(R.id.tv_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.tv_title)).check(ViewAssertions.matches(withText(detailTvShow.name)))

            for (genre in detailTvShow.genres){
                genres.add(genre.name)
            }

            Espresso.onView(withId(R.id.tv_info)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.tv_info)).check(ViewAssertions.matches(withText("${detailTvShow.firstAirDate} • ${genres.toString().replace("[", "").replace("]", "")} • ${detailTvShow.episodeRunTime[0]}m")))

            Espresso.onView(withId(R.id.tv_overview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.tv_overview)).check(ViewAssertions.matches(withText(detailTvShow.overview)))

            Espresso.onView(withId(R.id.iv_poster)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

            for (creator in detailTvShow.createdBy){
                creators.add(creator.name)
            }

            Espresso.onView(withId(R.id.tv_creator)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.tv_creator)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(withText(creators.toString().replace("[", "").replace("]", ""))))

            Espresso.onView(withId(R.id.tv_user_score)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            Espresso.onView(withId(R.id.tv_user_score)).check(ViewAssertions.matches(withText(detailTvShow.voteAverage.toString())))
        }
    }

    @Test
    fun shareTvShow() {
        if(detailTvShow != null) {
            val expectedTvShowIntent = Matchers.allOf(
                    IntentMatchers.hasAction(Intent.ACTION_SEND),
                    IntentMatchers.hasExtra(Intent.EXTRA_TEXT, "Watch ${detailTvShow.name} on new Film App!"),
                    IntentMatchers.hasType("text/plain")
            )
            Espresso.onView(ViewMatchers.withText("TV Shows")).perform(ViewActions.click())
            Espresso.onView(withId(R.id.rv_tv_show)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            0,
                            ViewActions.click()
                    )
            )
            Intents.init()
            Espresso.onView(withId(R.id.btn_share)).perform(ViewActions.click())
            intended(chooser(expectedTvShowIntent))
            Intents.release()
        }
    }

    fun withDrawable(resourceId: Int): Matcher<View> {
        return DrawableMatcher(resourceId)
    }

    fun chooser(matcher: Matcher<Intent>): Matcher<Intent> {
        return allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(`is`(Intent.EXTRA_INTENT), matcher))
    }
}

class DrawableMatcher(resourceId: Int) : TypeSafeMatcher<View>() {
    private var expectedId = resourceId
    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) {
            return false
        }
        val imageView: ImageView = item as ImageView
        if (expectedId < 0) {
            return imageView.getDrawable() == null
        }
        val resources: Resources = item.getContext().getResources()
        val expectedDrawable: Drawable = resources.getDrawable(expectedId) ?: return false
        val bitmap = getBitmap(imageView.getDrawable())
        val otherBitmap = getBitmap(expectedDrawable)
        return bitmap.sameAs(otherBitmap)
    }

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ");
        description.appendValue(expectedId);
    }

    private fun getBitmap(drawable: Drawable) : Bitmap {
        val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }
}