package com.moviesapp.ui.favorite

import com.moviesapp.data.model.Movie

sealed class FavMovieIntent {
    data object FetchMovies : FavMovieIntent()
    data class FavMovies(val movie: Movie) : FavMovieIntent()
}

sealed class FavMovieViewState {
    data object IDLE : FavMovieViewState()
    data class MovieList(val movies: List<Movie>) : FavMovieViewState()
    data class Error(val error: String? = null) : FavMovieViewState()
    data object EmptyList : FavMovieViewState()
}
