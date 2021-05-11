package com.example.film.ui.film

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.film.R
import com.example.film.data.resources.remote.response.MovieResultsItem
import com.example.film.databinding.ItemFilmBinding
import com.example.film.ui.film.detail.DetailFilmActivity

class FilmAdapter : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    private val listFilms = ArrayList<MovieResultsItem>()

    fun setFilms(films: List<MovieResultsItem>?){
        if (films == null) return
        this.listFilms.clear()
        this.listFilms.addAll(films)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val itemFilmBinding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(itemFilmBinding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val films = listFilms[position]
        holder.bind(films)
    }

    override fun getItemCount(): Int = listFilms.size

    class FilmViewHolder(private val binding: ItemFilmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(film: MovieResultsItem){
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