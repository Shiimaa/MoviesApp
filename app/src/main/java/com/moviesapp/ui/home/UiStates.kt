package com.moviesapp.ui.home

import androidx.paging.PagingData
import com.moviesapp.data.model.Movie

sealed class MovieIntent {
    data object FetchMovies : MovieIntent()
    data class FavMovies(val movie: Movie) : MovieIntent()
}

sealed class MovieViewState {
    data object IDLE : MovieViewState()
    data object Loading : MovieViewState()
    data class MovieList(val movies: PagingData<Movie>) : MovieViewState()
    data class Error(val error: String? = null) : MovieViewState()
}
