package com.example.film.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object SortUtils {
    const val HIGHEST = "Highest"
    const val LOWEST = "Lowest"
    const val RANDOM = "Random"
    fun getSortedMovies(filter: String): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM favorite_movie ")
        if (filter == HIGHEST) {
            simpleQuery.append("ORDER BY vote_average DESC")
        } else if (filter == LOWEST) {
            simpleQuery.append("ORDER BY vote_average ASC")
        } else if (filter == RANDOM) {
            simpleQuery.append("ORDER BY RANDOM()")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
    fun getSortedTvShows(filter: String): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM favorite_tv_show ")
        if (filter == HIGHEST) {
            simpleQuery.append("ORDER BY vote_average DESC")
        } else if (filter == LOWEST) {
            simpleQuery.append("ORDER BY vote_average ASC")
        } else if (filter == RANDOM) {
            simpleQuery.append("ORDER BY RANDOM()")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}