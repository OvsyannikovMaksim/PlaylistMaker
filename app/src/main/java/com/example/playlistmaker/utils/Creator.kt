package com.example.playlistmaker.utils

import com.example.playlistmaker.data.SongsRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitITunes
import com.example.playlistmaker.domain.api.SongInteractor
import com.example.playlistmaker.domain.api.SongsRepository
import com.example.playlistmaker.domain.impl.SongInteractorImpl

object Creator {
    private fun getSongsRepository(): SongsRepository = SongsRepositoryImpl(RetrofitITunes)

    fun getSongInteractor(): SongInteractor = SongInteractorImpl(getSongsRepository())
}
