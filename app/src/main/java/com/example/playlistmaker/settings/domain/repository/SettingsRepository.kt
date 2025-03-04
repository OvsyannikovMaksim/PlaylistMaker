package com.example.playlistmaker.settings.domain.repository

interface SettingsRepository {
    fun getThemeSettings(): Boolean

    fun updateThemeSetting(isDarkTheme: Boolean)

    fun getIsFirstRun(): Boolean

    fun putFirstRun(isFirstRun: Boolean)
}
