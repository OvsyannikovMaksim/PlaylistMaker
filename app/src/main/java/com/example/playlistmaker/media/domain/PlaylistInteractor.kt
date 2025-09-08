package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlayList(playlist: Playlist)
    suspend fun removePlayList(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>
}