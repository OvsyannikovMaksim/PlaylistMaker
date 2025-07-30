package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.domain.repository.SongsRepository
import com.example.playlistmaker.search.data.dto.SongRequest
import com.example.playlistmaker.search.data.dto.SongResponse
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SongsRepositoryImpl(
    private val networkClient: NetworkClient,
) : SongsRepository {
    override fun searchSong(expression: String): Flow<ArrayList<Track>> = flow {
        val response = networkClient.doRequest(SongRequest(expression))
        when (response.resultCode) {
            200 -> {
                val data = (response as SongResponse).results.map {
                    Track(
                        it.trackName,
                        it.artistName,
                        Utils.timeConverter(it.trackTimeMillis),
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl,
                    )
                }
                emit(data as ArrayList<Track>)
            }

            else -> throw Exception("Request Error")
        }
    }
}
