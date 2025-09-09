package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean

    suspend fun addPlayList(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getTracksInPlaylist(playlistId: Int): Flow<List<Track>>
    suspend fun deleteTrack(playlistId: Int, track: Track)

    suspend fun getPlaylistInfo(playlistId: Int): Flow<Playlist>

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun getTimeOfTrackInPlaylist(playlistId: Int): Int
}