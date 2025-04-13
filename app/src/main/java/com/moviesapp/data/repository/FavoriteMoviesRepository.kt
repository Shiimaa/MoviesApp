package com.moviesapp.data.repository

import com.moviesapp.data.model.Movie
import com.moviesapp.database.AppDatabase
import com.moviesapp.database.AppPreference
import com.moviesapp.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteMoviesRepository @Inject constructor(
    private val db: AppDatabase,
    private val sharedPreferences: AppPreference,
) {

    fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        val favoriteMoviesIds = sharedPreferences.getList(AppPreference.MOIVES_KEY)

        return db.getMoviesDao().getFavMovies(favoriteMoviesIds)
    }

    fun removeFromFavorite(movie: Movie) {
        sharedPreferences.removeIdFromList(AppPreference.MOIVES_KEY, movie.id)
    }
}