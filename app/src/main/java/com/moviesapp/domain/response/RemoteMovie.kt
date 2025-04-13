package com.moviesapp.domain.response

import android.os.Parcelable
import com.moviesapp.data.model.Movie
import com.moviesapp.database.entity.MovieEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemoteMovie(
    val id: Long,
    val original_title: String,
    val release_date: String,
    val poster_path: String?
) : Parcelable


fun RemoteMovie.toMovie(): Movie {
    return Movie(id, original_title, release_date, poster_path.orEmpty())
}

fun RemoteMovie.toDbMovie(isFav: Boolean = false): MovieEntity {
    return MovieEntity(id, original_title, release_date, poster_path.orEmpty(), isFav)
}