package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.HistoryRepository

class HistoryInteractorImpl(private val historyRepository: HistoryRepository) : HistoryInteractor {

    override fun getTrackHistory(): ArrayList<Track> {
        return historyRepository.getTrackHistory()
    }

    override fun putTrackToHistory(track: Track) {
        historyRepository.putTrackToHistory(track)
    }

    override fun clearTrackHistory() {
        historyRepository.clearTrackHistory()
    }
}