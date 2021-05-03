package com.example.film.ui.tv_show

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.remote.response.TvResultsItem

class TvShowViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    fun getTvShows(): LiveData<List<TvResultsItem>> = filmRepository.getTvs()
}