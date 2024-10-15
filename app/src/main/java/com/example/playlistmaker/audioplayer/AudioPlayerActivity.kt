package com.example.playlistmaker.audioplayer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.api.Track
import com.example.playlistmaker.utils.Utils.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private var trackInfo: Track? = null
    private lateinit var poster: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTime: TextView
    private lateinit var album: TextView
    private lateinit var albumTitle: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView

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

        poster = findViewById(R.id.poster)
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        trackTime = findViewById(R.id.track_time)
        genre = findViewById(R.id.genre)
        year = findViewById(R.id.year)
        album = findViewById(R.id.album)
        albumTitle = findViewById(R.id.album_title)
        country = findViewById(R.id.country)
        setTrack()
    }

    private fun getTrack(intent: Intent): Track? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(TRACK_INFO, Track::class.java)
        } else {
            intent.getSerializableExtra(TRACK_INFO) as Track
        }

    private fun setTrack() {
        Glide
            .with(this)
            .load(trackInfo?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(8.0F, this)))
            .into(poster)
        trackName.text = trackInfo?.trackName
        artistName.text = trackInfo?.artistName
        genre.text = trackInfo?.primaryGenreName
        year.text = trackInfo?.releaseDate?.slice(0..3)
        if (trackInfo?.collectionName == null)
            {
                album.isVisible = false
                albumTitle.isVisible = false
            } else {
            album.isVisible = true
            albumTitle.isVisible = true
        }
        album.text = trackInfo?.collectionName
        country.text = trackInfo?.country
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackInfo?.trackTimeMillis)
    }
}
