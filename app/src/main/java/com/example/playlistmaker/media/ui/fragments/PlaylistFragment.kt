package com.example.playlistmaker.media.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private val clickListener =
        object : TrackListAdapter.TrackClickListener {
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

    private val shareClickListener = object : PlaylistViewModel.ShareClick {
        override fun onEmptyList() {
            Toast.makeText(context, R.string.empty_track_list_toast_text, Toast.LENGTH_SHORT).show()
        }

        override fun onList(string: String) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "plain/text"
                putExtra(Intent.EXTRA_TEXT, string)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(
                Intent.createChooser(
                    intent,
                    requireContext().getString(R.string.chooser_text)
                )
            )
        }
    }

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
        viewModel.getState().observe(viewLifecycleOwner) {
            Log.d("TEST", it.playlist.toString())
            setUpUi(it.playlist, it.sumTimeImMin)
            setUpTrackBottomSheet(it.tracks)
            setUpOptionBottomSheet(it.playlist)
        }
    }

    private fun setUpUi(playlist: Playlist, minutes: Int) {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
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
            playlistTime.text = requireContext().resources.getQuantityString(
                R.plurals.track_sum_time,
                minutes,
                minutes
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
            viewModel.share(shareClickListener)
        }
        binding.optionsButton.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheetOption).apply {
                isFitToContents = false
                halfExpandedRatio = 0.5f
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        binding.editButtonBsh.setOnClickListener{
            findNavController().navigate(R.id.action_playlistFragment_to_changePlaylistFragment, bundleOf("playlistId" to args.playlist.id))
        }
    }

    private fun setUpTrackBottomSheet(trackList: List<Track>) {
        BottomSheetBehavior.from(binding.bottomSheet).apply {
            isFitToContents = false
            halfExpandedRatio = 0.32f
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        binding.tracksRv.isVisible = trackList.isNotEmpty()
        binding.tracksRv.adapter = TrackListAdapter(trackList, clickListener)
    }

    private fun setUpOptionBottomSheet(playlist: Playlist) {
        BottomSheetBehavior.from(binding.bottomSheetOption).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object :
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
        binding.apply {
            playlistNameBsh.text = playlist.name
            playlistTrackAmountBsh.text = requireContext().resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracksAmount,
                playlist.tracksAmount
            )
            shareButtonBsh.setOnClickListener {
                viewModel.share(shareClickListener)
            }
            deleteButtonBsh.setOnClickListener{
                showDeletePlaylistAlert()
            }
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
            .transform(RoundedCorners(Utils.dpToPx(2.0F, requireContext())))
            .into(binding.playlistImageBsh)
    }

    private fun showDeleteTrackAlert(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.dialog_delete_track_message)
            .setNegativeButton(R.string.dialog_no) { _, _ -> }
            .setPositiveButton(R.string.dialog_yes) { _, _ ->
                viewModel.deleteTrackFromPlaylist(args.playlist, track)
            }
            .show()
    }

    private fun showDeletePlaylistAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.dialog_delete_track_message)
            .setNegativeButton(R.string.dialog_no) { _, _ -> }
            .setPositiveButton(R.string.dialog_yes) { _, _ ->
                viewModel.deletePlaylist()
                findNavController().popBackStack()
            }
            .show()
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
