package com.example.playlistmaker.data.settings

interface SettingsRepository {
    fun getThemeSettings(): Boolean

    fun updateThemeSetting(isDarkTheme: Boolean)
}
