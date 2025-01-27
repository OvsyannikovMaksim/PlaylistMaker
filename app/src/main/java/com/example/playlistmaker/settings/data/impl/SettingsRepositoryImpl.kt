package com.example.playlistmaker.settings.data.impl

import android.app.Application
import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.main.data.SharedPreferences
import com.example.playlistmaker.settings.data.SettingsRepository

class SettingsRepositoryImpl(
    private val context: Context,
    private val application: Application,
) : SettingsRepository {
    override fun getThemeSettings(): Boolean = SharedPreferences.getNightMode(context)

    override fun updateThemeSetting(isDarkTheme: Boolean) {
        (application as App).switchTheme(isDarkTheme)
        SharedPreferences.putNightMode(context, isDarkTheme)
    }
}
