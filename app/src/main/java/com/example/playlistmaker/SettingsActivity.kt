package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val shareButton = findViewById<TextView>(R.id.share_button)
        val supportButton = findViewById<TextView>(R.id.support_button)
        val eulaButton = findViewById<TextView>(R.id.eula_button)

        shareButton.setOnClickListener {
            startShareIntent()
        }

        supportButton.setOnClickListener {
            startSupportIntent()
        }

        eulaButton.setOnClickListener {
            startEulaIntent()
        }
    }

    private fun startShareIntent() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer")
        intent.setType("text/plain")
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun startSupportIntent() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("maksimov99997@yandex.ru"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
        intent.putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
        startActivity(intent)
    }

    private fun startEulaIntent()  {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("https://yandex.ru/legal/practicum_offer/"))
        startActivity(intent)
    }
}
