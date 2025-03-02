package com.example.playlistmaker.utils

import android.app.Application
import android.content.Context
import com.example.playlistmaker.audioplayer.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.audioplayer.domain.MediaPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.audioplayer.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.search.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker.search.domain.repository.SongsRepository
import com.example.playlistmaker.search.data.impl.SongsRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitITunes
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.SongInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SongInteractorImpl
import com.example.playlistmaker.search.domain.repository.HistoryRepository
import com.example.playlistmaker.settings.domain.repository.SettingsRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private fun getSongsRepository(): SongsRepository = SongsRepositoryImpl(RetrofitITunes)

    private fun getExternalNavigator(context: Context): ExternalNavigator = ExternalNavigatorImpl(context)

    private fun getSettingsRepository(
        context: Context,
        application: Application,
    ): SettingsRepository = SettingsRepositoryImpl(context, application)

    fun getSongInteractor(): SongInteractor = SongInteractorImpl(getSongsRepository())

    fun getSharingInteractor(context: Context): SharingInteractor = SharingInteractorImpl(getExternalNavigator(context))

    fun getSettingsInteractor(
        context: Context,
        application: Application,
    ): SettingsInteractor = SettingsInteractorImpl(getSettingsRepository(context, application))

    fun getMediaPlayerInteractor(): MediaPlayerInteractor =
        MediaPlayerInteractorImpl(getMediaPlayerRepository())

    private fun getMediaPlayerRepository(): MediaPlayerRepository =
        MediaPlayerRepositoryImpl()

    fun getHistoryInteractor(context: Context): HistoryInteractor =
        HistoryInteractorImpl(getHistoryRepository(context))

    private fun getHistoryRepository(context: Context): HistoryRepository=
        HistoryRepositoryImpl(context)
}
