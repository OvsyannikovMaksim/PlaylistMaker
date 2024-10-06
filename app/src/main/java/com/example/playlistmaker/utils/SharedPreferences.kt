package com.example.playlistmaker.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {
    private fun getSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(MYSHAREDPREF, 0)

    fun putNightMode(
        context: Context,
        isNightMode: Boolean,
    ) {
        getSharedPreferences(context).edit().putBoolean(NIGTHMODE, isNightMode).apply()
    }

    fun getNightMode(context: Context): Boolean = getSharedPreferences(context).getBoolean(NIGTHMODE, false)

    private const val MYSHAREDPREF = "PLAYLISTSHAREDPREF"
    private const val NIGTHMODE = "NIGHTMODE"
}
