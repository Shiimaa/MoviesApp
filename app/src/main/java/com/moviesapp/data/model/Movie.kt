package com.moviesapp.data.model

import com.moviesapp.database.entity.MovieEntity


data class Movie(
    val id: Long,
    val title: String,
    val releaseDate: String,
    val imagePath: String,
    var isFavorite: Boolean = false
)

fun Movie.toDbMovie() =
    MovieEntity(id, title, releaseDate, imagePath, isFavorite)