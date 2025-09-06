package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.audioplayer.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.audioplayer.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.media.data.FavTracksRepositoryImpl
import com.example.playlistmaker.media.data.PlaylistRepositoryImpl
import com.example.playlistmaker.media.domain.repository.FavTracksRepository
import com.example.playlistmaker.media.domain.repository.PlaylistRepository
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.SongsRepositoryImpl
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.data.network.RetrofitITunes
import com.example.playlistmaker.search.domain.repository.HistoryRepository
import com.example.playlistmaker.search.domain.repository.SongsRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.repository.SettingsRepository
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://itunes.apple.com"
private const val MYSHAREDPREF = "PLAY_LIST_SHARED_PREF"

val dataModule = module {

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get(), get())
    }

    factory<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    factory<SongsRepository> {
        SongsRepositoryImpl(get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get(), androidApplication())
    }

    factory<FavTracksRepository> {
        FavTracksRepositoryImpl(get())
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(get())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single<ITunesApi> {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            MYSHAREDPREF,
            Context.MODE_PRIVATE,
        )
    }

    factory<NetworkClient> {
        RetrofitITunes(get())
    }

    factory {
        MediaPlayer()
    }

    factory {
        Gson()
    }

    single<AppDatabase> { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "track_database.db").build() }
}