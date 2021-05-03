package com.example.film.data

data class FilmEntity (
    var filmId: String,
    var title: String,
    var releaseDate: String,
    var overview: String,
    var genre: String,
    var poster: Int,
    var director: String,
    var userScore: String,
    var duration: String
)