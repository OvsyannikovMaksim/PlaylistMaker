package com.example.playlistmaker.main.data

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferences {
    private fun getSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(
            MYSHAREDPREF,
            Context.MODE_PRIVATE,
        )

    private val gson = Gson()

    fun putNightMode(
        context: Context,
        isNightMode: Boolean,
    ) {
        getSharedPreferences(context).edit().putBoolean(NIGTHMODE, isNightMode).apply()
    }

    fun getNightMode(context: Context): Boolean = getSharedPreferences(context).getBoolean(NIGTHMODE, false)


    fun getTrackHistory(context: Context): ArrayList<Track> {
        val json = getSharedPreferences(context).getString(TRACKHISTORY, null)
        return json?.let { gson.fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type) }
            ?: arrayListOf()
    }

    fun registerChangeListener(
        context: Context,
        listener: OnSharedPreferenceChangeListener,
    ) {
        getSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterChangeListener(
        context: Context,
        listener: OnSharedPreferenceChangeListener,
    ) {
        getSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun getTrackHistoryTag(): String = TRACKHISTORY

    private const val MYSHAREDPREF = "PLAYLISTSHAREDPREF"
    private const val NIGTHMODE = "NIGHTMODE"
    private const val TRACKHISTORY = "TRACKHISTORY"
}
