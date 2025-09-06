package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.repository.FavTracksRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavTracksInteractorImpl(val favTracksRepository: FavTracksRepository): FavTracksInteractor {

    override suspend fun getFavTracks(): Flow<List<Track>> {
        return favTracksRepository.getFavTracks()
    }
}