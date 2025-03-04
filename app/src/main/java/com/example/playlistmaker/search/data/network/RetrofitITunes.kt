package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SongRequest

class RetrofitITunes(private val iTunesApi: ITunesApi) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is SongRequest) {
            val response = iTunesApi.search(dto.expression).execute()
            val result = response.body() ?: Response()
            return result.apply {
                resultCode = response.code()
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}
