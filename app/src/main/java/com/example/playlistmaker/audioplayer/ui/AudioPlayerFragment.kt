package com.example.playlistmaker.audioplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.media.ui.adapter.PlaylistBottomSheetAdapter
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AudioPlayerViewModel>()

    private var trackInfo: Track? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        val args: AudioPlayerFragmentArgs by navArgs()
        trackInfo = args.audioArgs
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getPlaylists()
        hideBottomSheet()

        viewModel.getPlayerState().observe(viewLifecycleOwner) {
            binding.playButton.background = getDrawable(
                requireContext(),
                it.buttonImageRes,
            )
            binding.currentTime.text = it.currentTime
        }

        viewModel.getFavState().observe(viewLifecycleOwner) {
            when (it) {
                false -> binding.likeButton.setBackgroundResource(R.drawable.like_button)
                else -> binding.likeButton.setBackgroundResource(R.drawable.like_button_tapped)
            }
        }

        viewModel.playlists().observe(viewLifecycleOwner) {
            binding.playlistsRv.isVisible = !it.isNullOrEmpty()
            binding.playlistsRv.adapter = PlaylistBottomSheetAdapter(it) { playlist ->
                viewModel.addTrackToPlaylist(playlist, trackInfo!!)
            }
        }

        viewModel.isAddedToPlaylist().observe(viewLifecycleOwner) {
            when (it.needShow) {
                true -> {
                    Toast.makeText(
                        requireContext(),
                        "Added to playlist '${it.playlistName}'",
                        Toast.LENGTH_LONG
                    ).show()
                    hideBottomSheet()
                }

                false -> {
                    Toast.makeText(
                        requireContext(),
                        "Already in playlist '${it.playlistName}'",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {}
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
            hideBottomSheet()
            findNavController().navigate(R.id.action_audioPlayerActivity_to_addPlaylistFragment)
        }
        binding.playlistButton.setOnClickListener {
            viewModel.getPlaylists()
            BottomSheetBehavior.from(binding.bottomSheet).apply {
                isFitToContents = false
                halfExpandedRatio = 0.6f
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        BottomSheetBehavior.from(binding.bottomSheet).addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

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
        _binding = null
    }

    private fun setTrack() {
        Glide
            .with(this)
            .load(trackInfo?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(8.0F, requireContext())))
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

    private fun hideBottomSheet() {
        BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
    }

    companion object {
        const val NAV_TAG = "audioArgs"
    }
}
