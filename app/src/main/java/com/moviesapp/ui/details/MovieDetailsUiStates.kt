package com.moviesapp.ui.details

import com.moviesapp.data.model.Movie
import com.moviesapp.data.model.MovieDetails

sealed class MovieDetailsIntent {
    data class FetchMovieDetails(val movieId: Long) : MovieDetailsIntent()
}

sealed class MovieDetailsViewState {
    data class Success(val movie: MovieDetails) : MovieDetailsViewState()
    data class Error(val error: String? = null) : MovieDetailsViewState()
    data object Loading : MovieDetailsViewState()
}
