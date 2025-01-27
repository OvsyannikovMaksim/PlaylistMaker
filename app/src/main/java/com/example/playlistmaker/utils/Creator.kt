package com.example.playlistmaker.utils

import android.app.Application
import android.content.Context
import com.example.playlistmaker.data.search.impl.SongsRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitITunes
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.search.SongInteractor
import com.example.playlistmaker.data.search.SongsRepository
import com.example.playlistmaker.domain.search.impl.SongInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl

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
}
