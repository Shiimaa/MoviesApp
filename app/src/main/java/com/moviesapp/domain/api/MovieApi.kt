package com.moviesapp.domain.api

import com.moviesapp.domain.response.MovieDetailsResponse
import com.moviesapp.domain.response.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    @GET("discover/movie")
    suspend fun getMoviesList(@Query("page") page: Int = 1): MoviesResponse


    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Long): MovieDetailsResponse


}