package com.moviesapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviesapp.data.model.Movie
import com.moviesapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MovieAdapter
    private var isVertical = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)

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
                    is MovieViewState.IDLE -> Log.d(HomeFragment::class.java.name, "State is IDLE")

                    is MovieViewState.Error -> Log.d(
                        HomeFragment::class.java.name,
                        "State is ERROR. Error:${state.error}"
                    )

                    is MovieViewState.Loading ->
                        binding.progressBar.visibility = View.VISIBLE

                    is MovieViewState.MovieList -> {
                        binding.progressBar.visibility = View.GONE

                        parentFragment?.viewLifecycleOwner?.lifecycle?.let {
                            adapter.submitData(it, state.movies)

                        }
                    }
                }
            }
        }

        viewModel.handleIntent(MovieIntent.FetchMovies)

    }

    private fun bindUiViews() {
        adapter = MovieAdapter(object : OnClickListener {
            override fun onItemClick(movie: Movie) {
            }

            override fun onFavClicked(movie: Movie) {
                viewModel.handleIntent(MovieIntent.FavMovies(movie))
            }
        })

        binding.rvMovie.setLayoutManager(LinearLayoutManager(requireContext()))
        binding.rvMovie.setAdapter(adapter)

        binding.ibToggleRvLayout.setOnClickListener {
            isVertical = !isVertical
            if (isVertical)
                binding.rvMovie.setLayoutManager(LinearLayoutManager(requireContext()))
            else
                binding.rvMovie.setLayoutManager(GridLayoutManager(requireContext(), 2))
        }
    }
}