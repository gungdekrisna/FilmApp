package com.example.film.data

data class TvShowEntity (
    var tvShowId: String,
    var title: String,
    var year: String,
    var overview: String,
    var genre: String,
    var poster: Int,
    var creator: String,
    var userScore: String,
    var duration: String,
)