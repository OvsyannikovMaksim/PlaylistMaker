package com.example.playlistmaker.di

import com.example.playlistmaker.audioplayer.domain.MediaPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.SongInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.SongInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val domainModule = module {

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    factory<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    factory<SongInteractor> {
        SongInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}