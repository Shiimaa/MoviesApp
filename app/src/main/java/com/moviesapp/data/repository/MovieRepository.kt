package com.moviesapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.moviesapp.data.model.Movie
import com.moviesapp.database.AppDatabase
import com.moviesapp.database.AppPreference
import com.moviesapp.domain.api.MovieApi
import javax.inject.Inject
import javax.inject.Singleton

const val PAGE_SIZE = 15

@Singleton
class MovieRepository @Inject constructor(
    private val db: AppDatabase,
    private val sharedPreferences: AppPreference,
    private val movieApi: MovieApi
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getMoviesList() =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { db.getMoviesDao().getAllMovies() },
            remoteMediator = MoviesRemoteMediator(db, sharedPreferences, movieApi)
        ).flow

    fun updateFavMovie(movie: Movie) {
        if (movie.isFavorite)
            sharedPreferences.setIdToList(AppPreference.MOIVES_KEY, movie.id)
        else
            sharedPreferences.removeIdFromList(AppPreference.MOIVES_KEY, movie.id)
    }

}