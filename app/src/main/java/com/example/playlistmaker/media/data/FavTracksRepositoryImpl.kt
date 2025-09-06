package com.example.playlistmaker.media.data

import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.media.domain.repository.FavTracksRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavTracksRepositoryImpl(private val database: AppDatabase) : FavTracksRepository {

    override suspend fun getFavTracks(): Flow<List<Track>> = flow {
        emit(database.trackDao().getAllTracks().map { map(it) }.reversed())
    }
}