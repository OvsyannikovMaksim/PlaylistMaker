package com.example.playlistmaker.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitITunes {
    private const val BASE_URL = "https://itunes.apple.com"
    private val iTunesRetrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val iTunesApi: ITunesApi
        get() = iTunesRetrofit.create(ITunesApi::class.java)
}
