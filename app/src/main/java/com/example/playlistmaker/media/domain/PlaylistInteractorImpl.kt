package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.domain.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistInteractorImpl(val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override suspend fun addPlayList(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistRepository.addPlayList(playlist)
        }
    }

    override suspend fun removePlayList(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistRepository.removePlayList(playlist)
        }
    }
}