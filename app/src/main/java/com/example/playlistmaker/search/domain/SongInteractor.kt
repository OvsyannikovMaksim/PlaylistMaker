package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SongInteractor {
    fun searchSong(
        expression: String
    ): Flow<ArrayList<Track>>
}
