package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SongRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitITunes : NetworkClient {
    private const val BASE_URL = "https://itunes.apple.com"
    private val iTunesRetrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val iTunesApi: ITunesApi
        get() = iTunesRetrofit.create(ITunesApi::class.java)

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
