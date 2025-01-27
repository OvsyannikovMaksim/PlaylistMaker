package com.example.playlistmaker.data.search.impl

import android.util.Log
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.SongRequest
import com.example.playlistmaker.data.dto.SongResponse
import com.example.playlistmaker.data.search.SongsRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils

class SongsRepositoryImpl(
    private val networkClient: NetworkClient,
) : SongsRepository {
    override fun searchSong(expression: String): List<Track> {
        val response = networkClient.doRequest(SongRequest(expression))
        Log.d("TEST", response.resultCode.toString())
        return if (response.resultCode == 200) {
            (response as SongResponse).results.map {
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
        } else {
            throw Exception("Request Error")
        }
    }
}
