package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMyPlaylistsBinding
import com.example.playlistmaker.media.ui.adapter.PlaylistAdapter
import com.example.playlistmaker.media.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.search.ui.adapter.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPlaylistsFragment : Fragment() {

    private var _binding: FragmentMyPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPlaylistsBinding.inflate(inflater, container, false)
        viewModel.getPlaylists()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_addPlaylistFragment)
        }

        val listener = PlaylistAdapter.PlayListListener { playlist ->
            findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment,
                bundleOf("playlist" to playlist)
            )
        }
        viewModel.playlists().observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            binding.placeholder.isVisible = it.isNullOrEmpty()
            binding.trackRv.isVisible = !it.isNullOrEmpty()
            binding.trackRv.adapter = PlaylistAdapter(it, listener)
            binding.trackRv.layoutManager = GridLayoutManager(requireActivity(),2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MyPlaylistsFragment()
    }
}