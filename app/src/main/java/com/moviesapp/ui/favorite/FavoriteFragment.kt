package com.moviesapp.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviesapp.R
import com.moviesapp.data.model.Movie
import com.moviesapp.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private val viewModel: FavoriteMoviesViewModel by viewModels()
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: FavoriteMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUiViews()
        bindViewModel()
    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is FavMovieViewState.IDLE -> Log.d(
                        FavoriteFragment::class.java.name,
                        "[AppPreference] State is IDLE"
                    )

                    is FavMovieViewState.Error -> {
                        binding.tvFailed.visibility = View.VISIBLE
                        binding.rvMovie.visibility = View.GONE
                        binding.tvFailed.text = state.error
                    }

                    is FavMovieViewState.MovieList -> {
                        binding.rvMovie.visibility = View.VISIBLE
                        binding.tvFailed.visibility = View.GONE
                        adapter.submitList(state.movies)
                    }

                    FavMovieViewState.EmptyList -> {
                        binding.tvFailed.visibility = View.VISIBLE
                        adapter.submitList(arrayListOf())
                    }
                }
            }
        }
        viewModel.handleIntent(FavMovieIntent.FetchMovies)
    }

    private fun bindUiViews() {
        adapter = FavoriteMoviesAdapter(object : FavoriteMoviesAdapter.OnItemClicked {

            override fun onClick(movieId: Long) {
                val bundle = Bundle()
                bundle.putLong("MOVIEID", movieId)
                findNavController(view!!).navigate(R.id.action_favoriteFragment_to_detailsFragment, bundle)
            }

            override fun onFavClick(movie: Movie) {
                viewModel.handleIntent(FavMovieIntent.FavMovies(movie))
            }
        })

        binding.rvMovie.setLayoutManager(LinearLayoutManager(requireContext()))
        binding.rvMovie.setAdapter(adapter)

    }
}