package com.moviesapp.di

import android.app.Application
import androidx.room.Room
import com.moviesapp.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providesDatabase(context: Application) =
        Room.databaseBuilder(context, AppDatabase::class.java, "MoviesDatabase")
            .build()

    @Provides
    @Singleton
    fun providesRemoteDao(database: AppDatabase) =
        database.remoteKeyDao()

    @Provides
    @Singleton
    fun providesMoviesDao(database: AppDatabase) =
        database.getMoviesDao()

}