package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.main.data.SharedPreferences

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (SharedPreferences.isFirstRun(this)) {
            SharedPreferences.putIsFirstRun(this, false)
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    SharedPreferences.putNightMode(this, true)
                }

                Configuration.UI_MODE_NIGHT_NO -> {
                    SharedPreferences.putNightMode(this, false)
                }
            }
        }
        switchTheme(SharedPreferences.getNightMode(this))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            },
        )
    }
}
