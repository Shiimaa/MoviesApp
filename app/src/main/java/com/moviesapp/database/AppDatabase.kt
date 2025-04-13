package com.moviesapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moviesapp.database.Dao.MoviesDao
import com.moviesapp.database.Dao.RemoteKeysDao
import com.moviesapp.database.entity.MovieEntity
import com.moviesapp.database.entity.RemoteKeys

@Database(entities = [MovieEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao
    abstract fun remoteKeyDao(): RemoteKeysDao
}