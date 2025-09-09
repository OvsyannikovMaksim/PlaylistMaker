package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlayList(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getTrackId(track: Track): Int?
    suspend fun getPlaylistId(playlist: Playlist): Int
    suspend fun isTrackInPlaylist(playlistId: Int, trackId: Int): Boolean
    suspend fun addTrack(track: Track)
    suspend fun addTrackToPlaylist(playlistId: Int, trackId: Int)
    suspend fun getTracksForPlaylist(playlistId: Int): Flow<List<Track>>
    suspend fun deleteTrackFromPlaylistToTrack(playlistId: Int, track: Track)
    suspend fun deleteTrackFromTracks(track: Track)
    suspend fun isAnyPlaylistContainsTrack(track: Track): Boolean
    suspend fun getPlaylistInfo(playlistId: Int): Flow<Playlist>
    suspend fun deletePlaylist(playlistId: Int)
    suspend fun deletePlaylistFromPlaylistToTrack(playlistId: Int)
}