package com.example.film.ui.film.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

    companion object{
        const val EXTRA_FILM = "extra_film"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.film_detail)

        var actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[DetailFilmViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {
            val filmId = extras.getInt(EXTRA_FILM)
            viewModel.setSelectedMovies(filmId)
            viewModel.getDetailMovies().observe(this, { film ->
                binding.progressBar.visibility = View.GONE
                populateFilm(film)
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
            .load("http://image.tmdb.org/t/p/original${detailMovieResponse.posterPath}")
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
}