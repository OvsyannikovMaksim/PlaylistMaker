package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean

    suspend fun addPlayList(playlist: Playlist)
    suspend fun removePlayList(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>
}