package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.adapter.TrackListAdapter
import com.example.playlistmaker.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val args: PlaylistFragmentArgs by navArgs()
    private val viewModel by viewModel<PlaylistViewModel>()
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getState(args.playlist.id)
        viewModel.getState().observe(viewLifecycleOwner){
            setUpUi(it.playlist)
            setUpTrackBottomSheet(it.tracks)
        }
    }
    private fun setUpUi(playlist: Playlist) {
        binding.apply {
            playlistTitle.text = playlist.name
            if (playlist.desc.isEmpty()) {
                playlistDesc.isVisible = false
            } else {
                playlistDesc.text = playlist.desc
            }
            playlistTrackAmount.text = requireContext().resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracksAmount,
                playlist.tracksAmount
            )
        }
        val imageUri = if (playlist.imagePath == null) {
            null
        } else {
            File(playlist.imagePath).toUri()
        }
        Glide.with(this)
            .load(imageUri)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(Utils.dpToPx(8.0F, requireContext())))
            .into(binding.playlistImage)
        binding.shareButton.setOnClickListener {
            viewModel.share()
        }
    }

    private fun setUpTrackBottomSheet(trackList: List<Track>){
        BottomSheetBehavior.from(binding.bottomSheet).apply {
            isFitToContents = false
            halfExpandedRatio = 0.35f
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        binding.tracksRv.isVisible = trackList.isNotEmpty()
        binding.tracksRv.adapter = TrackListAdapter(trackList, clickListener)
    }

    fun showDeleteTrackAlert(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.dialog_delete_track_message)
            .setNegativeButton(R.string.dialog_no) { dialog, which -> }
            .setPositiveButton(R.string.dialog_yes) { dialog, which ->
                viewModel.deleteTrackFromPlaylist(args.playlist, track)
            }
            .show()
    }

    private val clickListener =
        object: TrackListAdapter.TrackClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce()) {
                    findNavController().navigate(
                        R.id.action_playlistFragment_to_audioPlayerFragment,
                        bundleOf("audioArgs" to track)
                    )
                }
            }

            override fun onLongTrackClick(track: Track): Boolean {
                showDeleteTrackAlert(track)
                return true
            }
        }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isClickAllowed = true
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}