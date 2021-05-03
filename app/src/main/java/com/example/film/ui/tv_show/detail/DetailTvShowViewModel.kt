package com.example.film.ui.tv_show.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.film.data.resources.FilmRepository
import com.example.film.data.resources.remote.response.DetailTvResponse

class DetailTvShowViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    private var tvShowId: Int = 0

    fun setSelectedTvShow(tvShowId: Int) {
        this.tvShowId = tvShowId
    }

    fun getDetailTv() : LiveData<DetailTvResponse> = filmRepository.getDetailTv(tvShowId)
}