package com.example.film.ui.favorite.film

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.film.R
import com.example.film.data.resources.local.MovieEntity
import com.example.film.databinding.ItemFilmBinding
import com.example.film.ui.film.detail.DetailFilmActivity

class FavoriteFilmAdapter : PagedListAdapter<MovieEntity, FavoriteFilmAdapter.FavoriteFilmViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteFilmAdapter.FavoriteFilmViewHolder {
        val itemFilmBinding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteFilmAdapter.FavoriteFilmViewHolder(itemFilmBinding)
    }

    override fun onBindViewHolder(
        holder: FavoriteFilmAdapter.FavoriteFilmViewHolder,
        position: Int
    ) {
        val favoriteFilms = getItem(position)
        if (favoriteFilms != null) {
            holder.bind(favoriteFilms)
        }
    }

    class FavoriteFilmViewHolder(private val binding: ItemFilmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(film: MovieEntity){
            with(binding){
                tvTitle.text = film.title
                tvReleaseDate.text = film.releaseDate
                tvVote.text = film.voteAverage.toString()
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/original${film.posterPath}")
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error_image)
                    .into(imgPoster)

                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, DetailFilmActivity::class.java)
                    intent.putExtra(DetailFilmActivity.EXTRA_FILM, film.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}