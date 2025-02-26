package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track

interface HistoryInteractor {

    fun getTrackHistory(): ArrayList<Track>
    fun putTrackToHistory(track: Track)
    fun clearTrackHistory()
}