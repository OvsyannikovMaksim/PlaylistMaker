package com.example.playlistmaker.media.data

import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.domain.repository.PlaylistRepository
import com.example.playlistmaker.utils.Utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(private val database: AppDatabase) : PlaylistRepository {
    override suspend fun addPlayList(playlist: Playlist) {
        database.playlistDao().insertPlaylist(map(playlist))
    }

    override suspend fun removePlayList(playlist: Playlist) {
        database.playlistDao().deletePlaylist(map(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        emit(database.playlistDao().getPlaylists().map { map(it) })
    }
}