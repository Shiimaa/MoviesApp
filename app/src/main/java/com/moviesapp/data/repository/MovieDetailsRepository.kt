package com.moviesapp.data.repository

import com.moviesapp.data.model.MovieDetails
import com.moviesapp.domain.api.MovieApi
import com.moviesapp.domain.response.toMovieDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailsRepository @Inject constructor(
    private val movieApi: MovieApi
) {

    suspend fun getMovieDetails(movieId: Long): MovieDetails {
        val res = movieApi.getMovieDetails(movieId)
        return res.toMovieDetails()
    }
}