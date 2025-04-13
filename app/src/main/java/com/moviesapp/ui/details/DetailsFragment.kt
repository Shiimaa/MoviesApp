package com.moviesapp.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesapp.data.model.MovieDetails
import com.moviesapp.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private val viewModel: MoviesDetailsViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getLong("MOVIEID") ?: 0

        bindViewModel(movieId)

        binding.movieDetailsBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun bindViewModel(movieId: Long) {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is MovieDetailsViewState.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.layoutContainer.visibility = View.GONE
                    }

                    is MovieDetailsViewState.Error -> {
                        binding.txError.visibility = View.VISIBLE
                        binding.progress.visibility = View.GONE
                        binding.txError.text = state.error
                        binding.layoutContainer.visibility = View.GONE
                    }

                    is MovieDetailsViewState.Success -> {
                        binding.txError.visibility = View.GONE
                        binding.progress.visibility = View.GONE
                        binding.layoutContainer.visibility = View.VISIBLE
                        updateUi(state.movie)
                    }

                }
            }
        }
        viewModel.handleIntent(MovieDetailsIntent.FetchMovieDetails(movieId))
    }

    private fun updateUi(movie: MovieDetails) {
        Glide.with(binding.movieDetailsImage)
            .load("https://image.tmdb.org/t/p/original${movie.imagePath}")
            .into(binding.movieDetailsImage)

        binding.txMovieTitle.text = movie.title
        binding.movieDetailsMovieDescription.text = movie.overview
        binding.movieDetailsMovieAverageVote.text = movie.voteAverage.toString()
        binding.movieDetailsMovieVoteCount.text = movie.voteCount.toString()

        if (movie.genres.isNotEmpty()) {
            val adapter = MovieGensAdapter()
            binding.movieDetailsMovieGenreRv.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.movieDetailsMovieGenreRv.adapter = adapter
            adapter.submitList(movie.genres)
        }

    }

}