package com.example.playlistmaker.settings.data

import android.app.Application
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPreferences: android.content.SharedPreferences,
    private val application: Application,
) : SettingsRepository {
    override fun getThemeSettings(): Boolean = sharedPreferences.getBoolean(NIGTHMODE, false)

    override fun updateThemeSetting(isDarkTheme: Boolean) {
        (application as App).switchTheme(isDarkTheme)
        sharedPreferences.edit().putBoolean(NIGTHMODE, isDarkTheme).apply()
    }

    override fun getIsFirstRun(): Boolean = sharedPreferences.getBoolean(FIRST_RUN, true)

    override fun putFirstRun(isFirstRun: Boolean) {
        sharedPreferences.edit().putBoolean(FIRST_RUN, isFirstRun).apply()
    }

    companion object {
        private const val NIGTHMODE = "NIGHTMODE"
        private const val FIRST_RUN = "FIRST_RUN"
    }
}
