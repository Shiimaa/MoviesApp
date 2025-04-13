package com.moviesapp.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesapp.R
import com.moviesapp.data.model.Movie
import com.moviesapp.databinding.MovieItemBinding

class FavoriteMoviesAdapter(private val listener: OnItemClicked) :
    ListAdapter<Movie, FavoriteMoviesAdapter.FavoriteMovieViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        return FavoriteMovieViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.item.tvMovieTitle.text = movie.title
        holder.item.tvMovieReleaseDate.text = movie.releaseDate

        Glide.with(holder.item.ivMoviePoster)
            .load("https://image.tmdb.org/t/p/original${movie.imagePath}")
            .into(holder.item.ivMoviePoster)

        holder.itemView.setOnClickListener {
            listener.onClick(movie.id)
        }

        holder.item.ivMovieFavButton.setOnClickListener {
            listener.onFavClick(movie)
        }

        val favDrawable = if (movie.isFavorite)
            AppCompatResources.getDrawable(
                holder.item.ivMovieFavButton.context,
                R.drawable.ic_fill_favorite
            )
        else
            AppCompatResources.getDrawable(
                holder.item.ivMovieFavButton.context,
                R.drawable.ic_favorite
            )

        holder.item.ivMovieFavButton.background = favDrawable

    }

    class FavoriteMovieViewHolder(val item: MovieItemBinding) :
        RecyclerView.ViewHolder(item.root)

    interface OnItemClicked {
        fun onClick(movieId: Long)
        fun onFavClick(movie: Movie)
    }

}

class DiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}