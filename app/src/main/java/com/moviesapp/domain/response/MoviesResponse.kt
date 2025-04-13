package com.moviesapp.domain.response

import com.moviesapp.data.model.Movie


data class MoviesResponse(
    val page: Int,
    val results: List<RemoteMovie>
)
