package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavTracksRepository {

    suspend fun getFavTracks() : Flow<List<Track>>
}