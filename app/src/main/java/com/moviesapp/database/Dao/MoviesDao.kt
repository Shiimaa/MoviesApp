package com.moviesapp.database.Dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moviesapp.database.entity.MovieEntity

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<MovieEntity>)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movies")
    fun clearAll()
}