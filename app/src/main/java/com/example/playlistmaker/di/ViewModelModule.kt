package com.example.playlistmaker.di

import com.example.playlistmaker.audioplayer.ui.AudioPlayerViewModel
import com.example.playlistmaker.main.ui.MainViewModel
import com.example.playlistmaker.media.ui.view_model.FavTracksViewModel
import com.example.playlistmaker.media.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(get())
    }

    viewModel {
        MainViewModel(androidApplication())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavTracksViewModel()
    }
    viewModel {
        PlaylistViewModel()
    }
}