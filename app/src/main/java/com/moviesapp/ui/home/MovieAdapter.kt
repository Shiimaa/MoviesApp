package com.moviesapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesapp.R
import com.moviesapp.data.model.Movie
import com.moviesapp.databinding.MovieItemBinding

class MovieAdapter(private val listener: OnClickListener) :
    PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    inner class MovieViewHolder(
        private val binding: MovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }

            binding.ivMovieFavButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        item.isFavorite = !item.isFavorite
                        listener.onFavClicked(item)
                        checkFav(item.isFavorite)
                    }
                }
            }
        }

        fun bind(movie: Movie) {
            binding.tvMovieTitle.text = movie.title
            binding.tvMovieReleaseDate.text = movie.releaseDate

            Glide.with(binding.ivMoviePoster)
                .load("https://image.tmdb.org/t/p/original${movie.imagePath}")
                .into(binding.ivMoviePoster)

            checkFav(movie.isFavorite)
        }

        private fun checkFav(isFavorite: Boolean) {
            val favDrawable = if (isFavorite)
                AppCompatResources.getDrawable(
                    binding.ivMovieFavButton.context,
                    R.drawable.ic_fill_favorite
                )
            else
                AppCompatResources.getDrawable(
                    binding.ivMovieFavButton.context,
                    R.drawable.ic_favorite
                )

            binding.ivMovieFavButton.background = favDrawable
        }
    }
}

class MovieDiffCallBack : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}

interface OnClickListener {
    fun onItemClick(movie: Movie)
    fun onFavClicked(movie: Movie)
}
