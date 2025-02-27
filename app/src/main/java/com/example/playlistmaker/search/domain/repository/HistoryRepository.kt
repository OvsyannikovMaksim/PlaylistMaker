package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.model.Track

interface HistoryRepository {

    fun getTrackHistory(): ArrayList<Track>
    fun putTrackToHistory(track: Track)
    fun clearTrackHistory()
}