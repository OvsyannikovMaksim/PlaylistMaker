package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlayList(playlist: Playlist)
    suspend fun removePlayList(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
}