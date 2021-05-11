package com.example.film.ui.film.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.film.R
import com.example.film.data.resources.remote.response.DetailMovieResponse
import com.example.film.databinding.ActivityDetailFilmBinding
import com.example.film.viewmodel.ViewModelFactory

class DetailFilmActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailFilmBinding
    private var productionCompanies = ArrayList<String>()
    private var genres = ArrayList<String>()
    // private val viewModel by viewModel<DetailFilmViewModel>()
    private var menu: Menu? = null
    private lateinit var viewModel: DetailFilmViewModel
    private lateinit var detailMovie: DetailMovieResponse
    private var favorited: Boolean = false

    companion object{
        const val EXTRA_FILM = "extra_film"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.film_detail)

        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
            setDisplayHomeAsUpEnabled(true)
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailFilmViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {
            val filmId = extras.getInt(EXTRA_FILM)
            viewModel.setSelectedMovies(filmId)
            viewModel.getDetailMovies().observe(this, { film ->
                binding.progressBar.visibility = View.GONE
                populateFilm(film)
                detailMovie = film
            })
        }
    }

    private fun populateFilm(detailMovieResponse: DetailMovieResponse) {
        binding.tvTitle.text = detailMovieResponse.title

        for (genre in detailMovieResponse.genres){
            genres.add(genre.name)
        }

        binding.tvInfo.text = getString(R.string.info, detailMovieResponse.releaseDate, genres.toString().replace("[", "").replace("]", ""), detailMovieResponse.runtime)
        binding.tvOverview.text = detailMovieResponse.overview

        for (productionCompany in detailMovieResponse.productionCompanies){
            productionCompanies.add(productionCompany.name)
        }

        binding.tvProduction.text = productionCompanies.toString().replace("[", "").replace("]", "")
        binding.tvUserScore.text = detailMovieResponse.voteAverage.toString()
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original${detailMovieResponse.posterPath}")
            .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
            .error(R.drawable.ic_error_image)
            .into(binding.ivPoster)

        binding.btnShare.setOnClickListener {
            val shareTxt = resources.getString(R.string.share_film, detailMovieResponse.title)
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareTxt)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        val menuItem = menu?.findItem(R.id.action_favorite)
        this.menu = menu
        viewModel.getFavoriteMovie().observe(this, { favoriteMovie ->
            if (favoriteMovie != null){
                menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite)
                favorited = true
            } else {
                menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border)
                favorited = false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorite) {
            if (favorited){
                viewModel.deleteFavorite(detailMovie)
                item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border)
            } else {
                viewModel.setFavorite(detailMovie)
                item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}