package com.moviesapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.moviesapp.data.model.Movie
import com.moviesapp.data.repository.MovieRepository
import com.moviesapp.database.entity.toMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private val _state = MutableStateFlow<MovieViewState>(MovieViewState.IDLE)
    val state: StateFlow<MovieViewState> get() = _state


    fun handleIntent(intent: MovieIntent) {
        viewModelScope.launch {
            when (intent) {
                is MovieIntent.FetchMovies -> getMovies()
                is MovieIntent.FavMovies -> updateFavMovie(intent.movie)
            }
        }
    }

    private fun updateFavMovie(movie: Movie) {
        repository.updateFavMovie(movie)
    }

    private suspend fun getMovies() {
        try {
            _state.value = MovieViewState.Loading

            repository.getMoviesList()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _state.value = MovieViewState.MovieList(pagingData.map { it.toMovie() })
                }
        } catch (e: Exception) {
            _state.value = MovieViewState.Error(error = e.message ?: "Error fetching Movies")
        }
    }
}