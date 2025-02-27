package com.example.playlistmaker.main.data

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.google.gson.Gson

object SharedPreferences {
    private fun getSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(
            MYSHAREDPREF,
            Context.MODE_PRIVATE,
        )

    fun putNightMode(
        context: Context,
        isNightMode: Boolean,
    ) {
        getSharedPreferences(context).edit().putBoolean(NIGTHMODE, isNightMode).apply()
    }

    fun getNightMode(context: Context): Boolean = getSharedPreferences(context).getBoolean(NIGTHMODE, false)

    fun isFirstRun(context: Context): Boolean = getSharedPreferences(context).getBoolean(FIRST_RUN, true)
    fun putIsFirstRun(context: Context, isFirstRun: Boolean){
        getSharedPreferences(context).edit().putBoolean(FIRST_RUN, isFirstRun).apply()
    }

    private const val MYSHAREDPREF = "PLAYLISTSHAREDPREF"
    private const val NIGTHMODE = "NIGHTMODE"
    private const val FIRST_RUN = "FIRST_RUN"
}
