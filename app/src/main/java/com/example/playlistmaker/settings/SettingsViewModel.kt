package com.example.playlistmaker.settings

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.utils.SharedPreferences

class SettingsViewModel(
    private val application: Application,
) : AndroidViewModel(application) {
    fun startShareIntent() {
        val intent =
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, application.getString(R.string.practikum_link))
                type = "text/plain"
            }
        application.startActivity(Intent.createChooser(intent, application.getString(R.string.chooser_text)))
    }

    fun startSupportIntent() {
        val intent =
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(application.getString(R.string.email_to)))
                putExtra(Intent.EXTRA_SUBJECT, application.getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, application.getString(R.string.email_body))
            }
        application.startActivity(intent)
    }

    fun startEulaIntent() {
        val intent =
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(application.getString(R.string.eula_link))
            }
        application.startActivity(intent)
    }

    fun changeThemeMode(checked: Boolean) {
        (application as App).switchTheme(checked)
        SharedPreferences.putNightMode(application, checked)
    }
}
