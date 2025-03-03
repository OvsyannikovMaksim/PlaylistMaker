package com.example.playlistmaker.main.data

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(private val context: Context) {
    private fun getSharedPreferences(): SharedPreferences =
        context.getSharedPreferences(
            MYSHAREDPREF,
            Context.MODE_PRIVATE,
        )

    fun putNightMode(
        isNightMode: Boolean,
    ) {
        getSharedPreferences().edit().putBoolean(NIGTHMODE, isNightMode).apply()
    }

    fun getNightMode(): Boolean = getSharedPreferences().getBoolean(NIGTHMODE, false)

    fun isFirstRun(): Boolean = getSharedPreferences().getBoolean(FIRST_RUN, true)
    fun putIsFirstRun(isFirstRun: Boolean){
        getSharedPreferences().edit().putBoolean(FIRST_RUN, isFirstRun).apply()
    }

    companion object {
        private const val MYSHAREDPREF = "PLAYLISTSHAREDPREF"
        private const val NIGTHMODE = "NIGHTMODE"
        private const val FIRST_RUN = "FIRST_RUN"
    }
}
