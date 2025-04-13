package com.moviesapp.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesapp.R
import com.moviesapp.data.model.GenresItem
import com.moviesapp.data.model.Movie
import com.moviesapp.databinding.MovieGenresItemBinding
import com.moviesapp.databinding.MovieItemBinding

class MovieGensAdapter() :
    ListAdapter<GenresItem, MovieGensAdapter.MovieGensViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGensViewHolder {
        return MovieGensViewHolder(
            MovieGenresItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieGensViewHolder, position: Int) {
        val item = getItem(position)
        holder.item.movieGenernsText.text = item.name

    }

    class MovieGensViewHolder(val item: MovieGenresItemBinding) :
        RecyclerView.ViewHolder(item.root)

}

class DiffCallback : DiffUtil.ItemCallback<GenresItem>() {
    override fun areItemsTheSame(oldItem: GenresItem, newItem: GenresItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GenresItem, newItem: GenresItem): Boolean {
        return oldItem == newItem
    }
}