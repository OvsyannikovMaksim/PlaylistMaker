package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.media.ui.view_model.AddPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPlaylistFragment: Fragment() {

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AddPlaylistViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputPlaylistName.addTextChangedListener(
            onTextChanged = { _: CharSequence?, _: Int, _: Int, count: Int ->
                binding.addPlaylistButton.isEnabled = if (count > 0) {
                    true
                } else {
                    false
                }
            }
        )

        binding.addPlaylistButton.setOnClickListener {
            viewModel.savePlaylist()
        }
        binding.toolbar.setNavigationOnClickListener {
            if(binding.inputPlaylistName.text.isNullOrEmpty() && binding.inputPlaylistDesc.text.isNullOrEmpty()) {
                findNavController().popBackStack()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.add_playlist_dialog_title)
                    .setMessage(R.string.add_playlist_dialog_desc)
                    .setPositiveButton("Ok"){
                            _, _ ->
                        findNavController().popBackStack()

                    }.setNeutralButton("Cancel"){
                            _, _ ->
                    }
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}