package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.repository.SongsRepository
import com.example.playlistmaker.search.domain.SongInteractor
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class SongInteractorImpl(
    private val repository: SongsRepository,
) : SongInteractor {

    override fun searchSong(expression: String) : Flow<ArrayList<Track>> {
        return repository.searchSong(expression)
    }
}
