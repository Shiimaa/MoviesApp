package com.moviesapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.moviesapp.database.AppPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreference(context: Application) =
        AppPreference(context.getSharedPreferences("preferences_name", Context.MODE_PRIVATE))
}