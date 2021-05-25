package com.example.film.ui.favorite.tv_show

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.local.TvShowEntity

class FavoriteTvShowViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    fun getFavoriteTvShow(sort: String): LiveData<PagedList<TvShowEntity>> {
        return LivePagedListBuilder(filmRepository.getFavoriteTvShows(sort), 20).build()
    }
}