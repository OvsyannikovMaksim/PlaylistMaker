package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.domain.repository.PlaylistRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        return withContext(Dispatchers.IO) {
            val trackId = playlistRepository.getTrackId(track) ?: 0
            val playlistId = playlistRepository.getPlaylistId(playlist)
            val res = playlistRepository.isTrackInPlaylist(playlistId, trackId)
            if(!res) {
                playlistRepository.addPlayList(playlist)
                playlistRepository.addTrack(track)
                val trackIdNew = playlistRepository.getTrackId(track)?:0
                playlistRepository.addTrackToPlaylist(playlistId, trackIdNew)
            }
            !res
        }
    }

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

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }
}