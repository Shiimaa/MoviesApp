package com.moviesapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesapp.data.repository.MovieDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesDetailsViewModel @Inject constructor(
    private val repository: MovieDetailsRepository
) : ViewModel() {
    private val _state = MutableStateFlow<MovieDetailsViewState>(MovieDetailsViewState.Loading)
    val state: StateFlow<MovieDetailsViewState> get() = _state


    fun handleIntent(intent: MovieDetailsIntent) {
        viewModelScope.launch {
            when (intent) {
                is MovieDetailsIntent.FetchMovieDetails -> getMovieDetails(intent.movieId)
            }
        }
    }

    private suspend fun getMovieDetails(movieId: Long) {
        try {
            val res = repository.getMovieDetails(movieId)
            _state.value = MovieDetailsViewState.Success(res)
        } catch (e: Exception) {
            _state.value = MovieDetailsViewState.Error(error = e.message ?: "Error fetching Movies")
        }
    }
}