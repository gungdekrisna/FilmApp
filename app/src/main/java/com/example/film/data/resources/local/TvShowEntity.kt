package com.example.film.data.resources.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tv_show")
data class TvShowEntity (
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "poster_path")
    val posterPath: String,

    @ColumnInfo(name = "first_air_date")
    val firstAirDate: String,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int
)