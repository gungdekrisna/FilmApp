package com.example.film.ui.film

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.film.data.resources.FilmRepository
import com.example.film.resources.remote.response.MovieResultsItem

class FilmViewModel(private val filmRepository: FilmRepository) : ViewModel(){

    fun getMovies(): LiveData<List<MovieResultsItem>> = filmRepository.getMovies()

}