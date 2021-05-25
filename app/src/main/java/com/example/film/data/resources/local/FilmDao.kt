package com.example.film.data.resources.local

import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface FilmDao {
    // Movies
    @RawQuery(observedEntities = [MovieEntity::class])
    fun getFavoriteMovies(query: SupportSQLiteQuery): DataSource.Factory<Int, MovieEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteMovie(movie: MovieEntity)

    @Query("SELECT * FROM favorite_movie WHERE id = :movieId")
    fun getFavoriteMovieById(movieId: Int): MovieEntity

    @Delete
    fun deleteFavoriteMovie(movie: MovieEntity)

    // Tv Shows
    @RawQuery(observedEntities = [TvShowEntity::class])
    fun getFavoriteTvShows(query: SupportSQLiteQuery): DataSource.Factory<Int, TvShowEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteTvShow(tvShow: TvShowEntity)

    @Query("SELECT * FROM favorite_tv_show WHERE id = :tvShowId")
    fun getFavoriteTvShowById(tvShowId: Int): TvShowEntity

    @Delete
    fun deleteFavoriteTvShow(tvShow: TvShowEntity)
}