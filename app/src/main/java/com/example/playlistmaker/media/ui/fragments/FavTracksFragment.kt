package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavTracksBinding
import com.example.playlistmaker.media.ui.view_model.FavTracksViewModel
import com.example.playlistmaker.search.ui.adapter.TrackListAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment : Fragment() {

    private var _binding: FragmentFavTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavTracksViewModel>()
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavTracksBinding.inflate(inflater, container, false)
        binding.trackRv.isVisible = false
        binding.placeholder.isVisible = false
        binding.progressBar.isVisible = true
        viewModel.getFavTracks()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.favTracks().observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            if (it.isNotEmpty()) {
                binding.trackRv.adapter = TrackListAdapter(it, clickListener)
                binding.trackRv.isVisible = true
                binding.placeholder.isVisible = false
            } else {
                binding.trackRv.isVisible = false
                binding.placeholder.isVisible = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val clickListener =
        TrackListAdapter.TrackClickListener { track ->
            if (clickDebounce()) {
                findNavController().navigate(
                    R.id.action_mediaFragment_to_audioPlayerActivity,
                    bundleOf("audioArgs" to track)
                )
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

    companion object {
        fun newInstance() = FavTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}