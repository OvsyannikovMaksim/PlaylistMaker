package com.example.playlistmaker.media.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavTracksInteractor {

    suspend fun getFavTracks(): Flow<List<Track>>
}