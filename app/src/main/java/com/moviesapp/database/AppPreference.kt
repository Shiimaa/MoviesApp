package com.moviesapp.database

import android.content.SharedPreferences
import javax.inject.Inject

class AppPreference @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun getStoredTag(tag: String): String {
        return sharedPreferences.getString(tag, null).orEmpty()
    }

    fun setStoredTag(tag: String, query: String) {
        sharedPreferences.edit().putString(tag, query).commit()
    }

    fun removeStoredTag(tag: String) {
        sharedPreferences.edit().remove(tag).commit()
    }
}