package com.example.playlistmaker.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.api.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferences {
    fun getSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(MYSHAREDPREF, 0)

    fun putNightMode(
        context: Context,
        isNightMode: Boolean,
    ) {
        getSharedPreferences(context).edit().putBoolean(NIGTHMODE, isNightMode).apply()
    }

    fun getNightMode(context: Context): Boolean = getSharedPreferences(context).getBoolean(NIGTHMODE, false)

    fun putTrackToHistory(
        context: Context,
        track: Track,
    ) {
        val trackList = getTrackHistory(context)
        trackList.remove(track)
        if (trackList.size < 10) {
            trackList.add(0, track)
        } else {
            trackList.apply {
                removeAt(trackList.size - 1)
                add(0, track)
            }
        }
        getSharedPreferences(context).edit().putString(TRACKHISTORY, Gson().toJson(trackList)).apply()
    }

    fun getTrackHistory(context: Context): ArrayList<Track> {
        val json = getSharedPreferences(context).getString(TRACKHISTORY, null)
        return if (json != null) {
            Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
        } else {
            arrayListOf()
        }
    }

    fun clearTrackHistory(context: Context) {
        getSharedPreferences(context).edit().remove(TRACKHISTORY).apply()
    }

    private const val MYSHAREDPREF = "PLAYLISTSHAREDPREF"
    private const val NIGTHMODE = "NIGHTMODE"
    const val TRACKHISTORY = "TRACKHISTORY"
}
