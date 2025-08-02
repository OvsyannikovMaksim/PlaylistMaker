package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SongRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.jvm.Throws

class RetrofitITunes(private val iTunesApi: ITunesApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if(dto !is SongRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO){
            try {
                iTunesApi.search(dto.expression).apply { resultCode = 200 }
            } catch (e: Throwable){
                Response().apply { resultCode = 400 }
            }
        }
    }
}
