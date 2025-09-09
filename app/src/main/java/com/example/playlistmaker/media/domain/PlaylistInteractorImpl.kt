package com.example.playlistmaker.media.domain

import android.util.Log
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.domain.repository.PlaylistRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        return withContext(Dispatchers.IO) {
            Log.d("TEST", "addTrackToPlaylist: $playlist, $track")
            var trackId = playlistRepository.getTrackId(track) ?: 0
            val res = playlistRepository.isTrackInPlaylist(playlist.id, trackId)
            if (!res) {
                playlistRepository.addPlayList(playlist)
                if (trackId == 0) {
                    playlistRepository.addTrack(track)
                    trackId = playlistRepository.getTrackId(track) ?: 0
                }
                playlistRepository.addTrackToPlaylist(playlist.id, trackId)
            }
            !res
        }
    }

    override suspend fun addPlayList(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistRepository.addPlayList(playlist)
        }
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun getTracksInPlaylist(playlistId: Int): Flow<List<Track>> {
        return playlistRepository.getTracksForPlaylist(playlistId)
    }

    override suspend fun deleteTrack(playlistId: Int, track: Track) {
        playlistRepository.deleteTrackFromPlaylistToTrack(playlistId, track)
        if (playlistRepository.isAnyPlaylistContainsTrack(track)) {
            playlistRepository.deleteTrackFromTracks(track)
        }
    }

    override suspend fun getPlaylistInfo(playlistId: Int): Flow<Playlist> {
        return playlistRepository.getPlaylistInfo(playlistId)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistRepository.deletePlaylist(playlistId)
        playlistRepository.deletePlaylistFromPlaylistToTrack(playlistId)
    }
}