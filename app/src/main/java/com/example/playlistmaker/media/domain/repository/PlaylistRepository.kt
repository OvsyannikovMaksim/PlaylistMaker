package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlayList(playlist: Playlist)
    suspend fun removePlayList(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getTrackId(track: Track): Int?
    suspend fun getPlaylistId(playlist: Playlist): Int
    suspend fun isTrackInPlaylist(playlistId: Int, trackId: Int): Boolean
    suspend fun addTrack(track: Track)
    suspend fun addTrackToPlaylist(playlistId: Int, trackId: Int)
}