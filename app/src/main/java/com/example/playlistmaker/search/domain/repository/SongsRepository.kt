package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SongsRepository {
    fun searchSong(expression: String): Flow<ArrayList<Track>>
}
