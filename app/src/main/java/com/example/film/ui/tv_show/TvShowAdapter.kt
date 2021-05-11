package com.example.film.ui.tv_show

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.film.R
import com.example.film.data.resources.remote.response.TvResultsItem
import com.example.film.databinding.ItemFilmBinding
import com.example.film.ui.tv_show.detail.DetailTvShowActivity

class TvShowAdapter : RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder>()  {

    private val listTvShows = ArrayList<TvResultsItem>()

    fun setTvShows(tvShows: List<TvResultsItem>?){
        if (tvShows == null) return
        this.listTvShows.clear()
        this.listTvShows.addAll(tvShows)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvShowViewHolder {
        val itemFilmBinding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowViewHolder(itemFilmBinding)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        val tvShows = listTvShows[position]
        holder.bind(tvShows)
    }

    override fun getItemCount(): Int = listTvShows.size

    class TvShowViewHolder(private val binding: ItemFilmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TvResultsItem){
            with(binding){
                tvTitle.text = tvShow.name
                tvReleaseDate.text = tvShow.firstAirDate
                tvVote.text = tvShow.voteAverage.toString()
                Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/original${tvShow.posterPath}")
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                        .error(R.drawable.ic_error_image)
                        .into(imgPoster)

                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, DetailTvShowActivity::class.java)
                    intent.putExtra(DetailTvShowActivity.EXTRA_TV_SHOW, tvShow.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}