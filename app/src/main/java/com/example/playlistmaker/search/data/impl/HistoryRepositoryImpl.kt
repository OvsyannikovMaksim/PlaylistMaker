package com.example.playlistmaker.search.data.impl

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.HistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryRepositoryImpl(val context: Context, private val gson: Gson) : HistoryRepository {

    private fun getSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(
            HISTORY_SHARED_PREF,
            Context.MODE_PRIVATE,
        )

    override fun getTrackHistory(): ArrayList<Track> {
        val json = getSharedPreferences(context).getString(TRACK_HISTORY, null)
        return json?.let { gson.fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type) }
            ?: arrayListOf()
    }

    override fun putTrackToHistory(
        track: Track,
    ) {
        val trackList = getTrackHistory()
        trackList.remove(track)
        if (trackList.size < 10) {
            trackList.add(0, track)
        } else {
            trackList.apply {
                removeAt(trackList.size - 1)
                add(0, track)
            }
        }
        getSharedPreferences(context).edit().putString(TRACK_HISTORY, gson.toJson(trackList))
            .apply()
    }

    override fun clearTrackHistory() {
        getSharedPreferences(context)
            .edit().remove(TRACK_HISTORY).apply()
    }

    companion object {
        const val HISTORY_SHARED_PREF = "HISTORY_SHARED_PREF"
        private const val TRACK_HISTORY = "TRACKHISTORY"
    }
}