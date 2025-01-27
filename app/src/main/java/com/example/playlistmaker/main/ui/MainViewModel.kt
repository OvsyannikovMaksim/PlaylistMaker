package com.example.playlistmaker.main.ui

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel

class MainViewModel(
    private val application: Application,
) : AndroidViewModel(application) {

    fun startIntent(cls: Class<*>){
        val intent = Intent(application, cls)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }
}