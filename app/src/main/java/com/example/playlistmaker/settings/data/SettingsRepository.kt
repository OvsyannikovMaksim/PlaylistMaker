package com.example.playlistmaker.settings.data

interface SettingsRepository {
    fun getThemeSettings(): Boolean

    fun updateThemeSetting(isDarkTheme: Boolean)
}
