package com.moviesapp.database

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import javax.inject.Inject


class AppPreference @Inject constructor(private val sharedPreferences: SharedPreferences) {
    companion object {
        const val MOIVES_KEY = "movies"

    }

    fun getStoredTag(tag: String): String {
        return sharedPreferences.getString(tag, null).orEmpty()
    }

    fun setStoredTag(tag: String, query: String) {
        sharedPreferences.edit().putString(tag, query).commit()
    }

    fun removeStoredTag(tag: String) {
        sharedPreferences.edit().remove(tag).commit()
    }

    fun setIdToList(listKey: String, id: Long) {
        val gson = Gson()
        val savedList = getList(listKey)
        Log.d("AppPreference", "[setIdToList] savedList:$savedList")
        val res = savedList.toMutableList()
        res.add(id)
        Log.d("AppPreference", "[setIdToList] savedList:$res")
        val json = gson.toJson(res)
        Log.d("AppPreference", "[setIdToList] json:$json")

        sharedPreferences.edit().putString(listKey, json).commit()
    }

    fun removeIdFromList(listKey: String, id: Long) {
        val gson = Gson()
        val savedList = getList(listKey)
        Log.d("AppPreference", "[removeIdFromList] savedList:$savedList")
        val res = savedList.toMutableList()
        res.remove(id)
        Log.d("AppPreference", "[removeIdFromList] savedList:$res")
        val json = gson.toJson(res)
        sharedPreferences.edit().putString(listKey, json).commit()
    }

    fun isItemInList(listKey: String, id: Long): Boolean {
        val savedList = getList(listKey)
        return savedList.contains(id)
    }

    fun getList(listKey: String): List<Long> {
        val gson = Gson()
        val json = sharedPreferences.getString(listKey, "[]")
        Log.d("AppPreference", "[getList] savedList:$json")
        return gson.fromJson(json, Array<Long>::class.java).asList()
    }


}