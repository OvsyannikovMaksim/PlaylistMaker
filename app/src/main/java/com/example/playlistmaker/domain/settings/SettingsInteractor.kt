package com.example.playlistmaker.domain.settings

interface SettingsInteractor {
    fun getThemeSettings(): Boolean

    fun updateThemeSetting(isDarkTheme: Boolean)
}
