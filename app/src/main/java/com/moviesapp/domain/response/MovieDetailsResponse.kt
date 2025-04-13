package com.moviesapp.domain.response

import android.os.Parcelable
import com.moviesapp.data.model.GenresItem
import com.moviesapp.data.model.MovieDetails
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetailsResponse(
    val backdrop_path: String? = null,

    val overview: String? = null,

    val original_title: String? = null,

    val release_date: String? = null,

    val genres: List<RemoteGenresItem>? = null,

    val vote_average: Double,

    val runtime: Int,

    val id: Long,

    val vote_count: Int
) : Parcelable

@Parcelize
data class RemoteGenresItem(
    val name: String,
    val id: Int
) : Parcelable

fun MovieDetailsResponse.toMovieDetails(): MovieDetails {
    return MovieDetails(
        id,
        original_title.orEmpty(),
        backdrop_path.orEmpty(),
        overview.orEmpty(),
        release_date.orEmpty(),
        genres?.map { it.toGenresItem() }.orEmpty(),
        vote_average,
        runtime,
        vote_count
    )
}

fun RemoteGenresItem.toGenresItem(): GenresItem {
    return GenresItem(name, id)
}
