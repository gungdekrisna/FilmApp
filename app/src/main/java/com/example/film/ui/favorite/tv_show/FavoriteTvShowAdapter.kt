package com.example.film.ui.favorite.tv_show

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.film.R
import com.example.film.data.resources.local.TvShowEntity
import com.example.film.databinding.ItemFilmBinding
import com.example.film.ui.tv_show.detail.DetailTvShowActivity

class FavoriteTvShowAdapter : PagedListAdapter<TvShowEntity, FavoriteTvShowAdapter.FavoriteTvShowViewHolder>(FavoriteTvShowAdapter.DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvShowEntity>() {
            override fun areItemsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteTvShowAdapter.FavoriteTvShowViewHolder {
        val itemFilmBinding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteTvShowAdapter.FavoriteTvShowViewHolder(itemFilmBinding)
    }

    override fun onBindViewHolder(
        holder: FavoriteTvShowAdapter.FavoriteTvShowViewHolder,
        position: Int
    ) {
        val favoriteTvShows = getItem(position)
        if (favoriteTvShows != null) {
            holder.bind(favoriteTvShows)
        }
    }

    class FavoriteTvShowViewHolder(private val binding: ItemFilmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TvShowEntity){
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