package com.example.playlistmaker.settings.data

import android.app.Application
import com.example.playlistmaker.App
import com.example.playlistmaker.main.data.SharedPreferences
import com.example.playlistmaker.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val application: Application,
) : SettingsRepository {
    override fun getThemeSettings(): Boolean = sharedPreferences.getNightMode()

    override fun updateThemeSetting(isDarkTheme: Boolean) {
        (application as App).switchTheme(isDarkTheme)
        sharedPreferences.putNightMode(isDarkTheme)
    }
}
