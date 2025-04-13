package com.moviesapp.data.model


data class MovieDetails(
    val id: Long,
    val title: String,

    val imagePath: String,

    val overview: String,

    val releaseDate: String,

    val genres: List<GenresItem>,

    val voteAverage: Double,

    val runtime: Int,

    val voteCount: Int
)

data class GenresItem(
    val name: String,
    val id: Int
)
