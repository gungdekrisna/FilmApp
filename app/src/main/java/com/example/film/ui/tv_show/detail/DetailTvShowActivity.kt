package com.example.film.ui.tv_show.detail

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
import com.example.film.data.resources.remote.response.DetailTvResponse
import com.example.film.databinding.ActivityDetailTvShowBinding
import com.example.film.viewmodel.ViewModelFactory

class DetailTvShowActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailTvShowBinding
    private var creators = ArrayList<String>()
    private var genres = ArrayList<String>()
    private var menu: Menu? = null
    private lateinit var viewModel: DetailTvShowViewModel
    private lateinit var detailTvShow: DetailTvResponse
    private var favorited: Boolean = false

    companion object{
        const val EXTRA_TV_SHOW = "extra_tv"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTvShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.tv_detail)

        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
            setDisplayHomeAsUpEnabled(true)
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailTvShowViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {
            val tvShowId = extras.getInt(EXTRA_TV_SHOW)
            viewModel.setSelectedTvShow(tvShowId)
            viewModel.getDetailTv().observe(this, { tv ->
                binding.progressBar.visibility = View.GONE
                populateTvShow(tv)
                detailTvShow = tv
            })
        }
    }

    private fun populateTvShow(detailTvResponse: DetailTvResponse) {
        binding.tvTitle.text = detailTvResponse.name

        for (genre in detailTvResponse.genres){
            genres.add(genre.name)
        }

        binding.tvInfo.text = getString(R.string.info, detailTvResponse.firstAirDate, genres.toString().replace("[", "").replace("]", ""), detailTvResponse.episodeRunTime[0])
        binding.tvOverview.text = detailTvResponse.overview

        for (creator in detailTvResponse.createdBy){
            creators.add(creator.name)
        }

        binding.tvCreator.text = creators.toString().replace("[", "").replace("]", "")
        binding.tvUserScore.text = detailTvResponse.voteAverage.toString()
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/original${detailTvResponse.posterPath}")
            .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
            .error(R.drawable.ic_error_image)
            .into(binding.ivPoster)

        binding.btnShare.setOnClickListener {
            val shareTxt = resources.getString(R.string.share_film, detailTvResponse.name)
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
        viewModel.getFavoriteTvShow().observe(this, { favoriteTvShow ->
            if (favoriteTvShow != null){
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
                viewModel.deleteFavorite(detailTvShow)
                item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border)
                favorited = false
            } else {
                viewModel.setFavorite(detailTvShow)
                item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite)
                favorited = true
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}