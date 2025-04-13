package com.moviesapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesapp.data.model.Movie
import com.moviesapp.data.repository.FavoriteMoviesRepository
import com.moviesapp.database.entity.toMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMoviesViewModel @Inject constructor(
    private val repository: FavoriteMoviesRepository
) : ViewModel() {
    private val _state = MutableStateFlow<FavMovieViewState>(FavMovieViewState.IDLE)
    val state: StateFlow<FavMovieViewState> get() = _state


    fun handleIntent(intent: FavMovieIntent) {
        viewModelScope.launch {
            when (intent) {
                is FavMovieIntent.FetchMovies -> getMovies()
                is FavMovieIntent.FavMovies -> removeFromFavorite(intent.movie)
            }
        }
    }


    private suspend fun removeFromFavorite(movie: Movie) {
        repository.removeFromFavorite(movie)
        getMovies()
    }

    private suspend fun getMovies() {
        try {
            val res = repository.getFavoriteMovies()
            res.onEmpty {
                _state.value = FavMovieViewState.EmptyList
            }

            res.collect { data ->
                if (data.isEmpty())
                    _state.value = FavMovieViewState.EmptyList
                else {
                    _state.value = FavMovieViewState.MovieList(data.map { it.toMovie(true) })
                }
            }
        } catch (e: Exception) {
            _state.value = FavMovieViewState.Error(error = e.message ?: "Error fetching Movies")
        }
    }
}