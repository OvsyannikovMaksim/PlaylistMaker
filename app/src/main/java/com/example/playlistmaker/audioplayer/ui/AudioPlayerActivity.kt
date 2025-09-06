package com.example.playlistmaker.audioplayer.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.media.ui.adapter.PlaylistBottomSheetAdapter
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerActivity : AppCompatActivity() {
    private var _binding: ActivityAudioplayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AudioPlayerViewModel>()

    private var trackInfo: Track? = null

    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.bottomSheet) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        trackInfo = getTrack(intent)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.getPlaylists()

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.getPlayerState().observe(this) {
            binding.playButton.background = getDrawable(
                this,
                it.buttonImageRes,
            )
            binding.currentTime.text = it.currentTime
        }

        viewModel.getFavState().observe(this) {
            when (it) {
                false -> binding.likeButton.setBackgroundResource(R.drawable.like_button)
                else -> binding.likeButton.setBackgroundResource(R.drawable.like_button_tapped)
            }
        }

        viewModel.playlists().observe(this) {
            binding.playlistsRv.isVisible = !it.isNullOrEmpty()
            binding.playlistsRv.adapter = PlaylistBottomSheetAdapter(it) { playlist ->
                if (viewModel.addTrackToPlaylist(playlist, trackInfo!!)) {
                    Toast.makeText(
                        applicationContext,
                        "Added to playlist '${playlist.name}'",
                        Toast.LENGTH_LONG
                    ).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    viewModel.getPlaylists()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Already in playlist '${playlist.name}'",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        setTrack()
        prepareMediaPlayer()
        setLikeButton()

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClick()
        }
        binding.likeButton.setOnClickListener {
            trackInfo?.let { viewModel.onLikeButtonClick(it) }
        }
        binding.newPlaylistButton.setOnClickListener {
        }
        binding.playlistButton.setOnClickListener {
            bottomSheetBehavior.apply {
                isFitToContents = false
                halfExpandedRatio = 0.6f
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset.coerceIn(0f, 1f).coerceAtMost(1f)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
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
            .into(binding.poster)
        with(binding) {
            trackName.text = trackInfo?.trackName
            artistName.text = trackInfo?.artistName
            genre.text = trackInfo?.primaryGenreName
            year.text = trackInfo?.releaseDate?.slice(0..3)
            if (trackInfo?.collectionName == null) {
                album.isVisible = false
                albumTitle.isVisible = false
            } else {
                album.isVisible = true
                albumTitle.isVisible = true
            }
            binding.album.text = trackInfo?.collectionName
            binding.country.text = trackInfo?.country
            binding.trackTime.text = trackInfo?.trackTime
        }
    }

    private fun prepareMediaPlayer() {
        viewModel.prepareMediaPlayer(trackInfo?.previewUrl)
    }

    private fun setLikeButton() {
        trackInfo?.let { viewModel.setLikeButtonState(it) }
    }

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
}
