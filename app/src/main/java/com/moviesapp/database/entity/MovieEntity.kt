package com.moviesapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moviesapp.data.model.Movie

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val releaseDate: String,
    val imagePath: String,
    var isFavorite: Boolean = false
)

fun MovieEntity.toMovie(isFav: Boolean = isFavorite): Movie {
    return Movie(id, title, releaseDate, imagePath, isFav)
}