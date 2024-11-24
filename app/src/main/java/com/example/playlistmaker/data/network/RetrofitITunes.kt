package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.SongRequest
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
            val resp = iTunesApi.search(dto.expression).execute()
            val result = resp.body() ?: Response()
            return result.apply {
                resultCode = resp.code()
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}
