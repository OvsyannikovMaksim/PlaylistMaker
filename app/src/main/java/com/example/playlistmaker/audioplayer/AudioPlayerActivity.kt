package com.example.playlistmaker.audioplayer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.R
import com.example.playlistmaker.api.Track

class AudioPlayerActivity : AppCompatActivity() {
    private var trackInfo: Track? = null

    companion object {
        private const val TRACK_INFO = "TRACK_INFO"

        fun launch(
            context: Context,
            track: Track,
        ) = context.startActivity(
            Intent(context, AudioPlayerActivity::class.java)
                .putExtra(TRACK_INFO, track),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackInfo = getTrack(intent)
        setContentView(R.layout.activity_audioplayer)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        Log.d("TEST", trackInfo.toString())
    }

    private fun getTrack(intent: Intent): Track? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(TRACK_INFO, Track::class.java)
        } else {
            intent.getSerializableExtra(TRACK_INFO) as Track
        }
}
