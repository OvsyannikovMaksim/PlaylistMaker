package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.HistoryRepository

class HistoryInteractorImpl(historyRepository: HistoryRepository): HistoryInteractor {

    override fun getTrackHistory(): ArrayList<Track> {
        TODO("Not yet implemented")
    }

    override fun putTrackToHistory(track: Track) {
        TODO("Not yet implemented")
    }

    override fun clearTrackHistory() {
        TODO("Not yet implemented")
    }
}