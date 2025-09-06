package com.example.playlistmaker.media.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.ui.view_model.AddPlaylistViewModel
import com.example.playlistmaker.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class AddPlaylistFragment : Fragment() {

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AddPlaylistViewModel>()
    private var uri: Uri? = null
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        setPicture(uri)
        this.uri = uri
    }
    private var isImageSet: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputPlaylistName.addTextChangedListener(
            onTextChanged = { _: CharSequence?, _: Int, _: Int, count: Int ->
                binding.addPlaylistButton.isEnabled = count > 0
            }
        )

        binding.toolbar.setNavigationOnClickListener {
            if (binding.inputPlaylistName.text.isNullOrEmpty()
                && binding.inputPlaylistDesc.text.isNullOrEmpty()
                && !isImageSet
            ) {
                findNavController().popBackStack()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.add_playlist_dialog_title)
                    .setMessage(R.string.add_playlist_dialog_desc)
                    .setPositiveButton("Ok") { _, _ ->
                        findNavController().popBackStack()
                    }.setNeutralButton("Cancel") { _, _ ->
                    }.show()
            }
        }

        binding.addPicture.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.addPlaylistButton.setOnClickListener {
            val name = binding.inputPlaylistName.text.toString()
            val desc = binding.inputPlaylistDesc.text.toString()
            val imagePath = saveImageToPrivateStorage(uri)
            viewModel.savePlayList(Playlist(name, desc, imagePath, emptyList(), 0))
            Toast.makeText(requireContext(), "Playlist '${name}' was created", Toast.LENGTH_LONG)
                .show()
            findNavController().popBackStack()
        }
    }

    private fun setPicture(uri: Uri?) {
        isImageSet = true
        Glide.with(requireContext())
            .load(uri)
            .fitCenter()
            .error{
                isImageSet = false
            }
            .placeholder(R.drawable.add_photo_united)
            .transform(RoundedCorners(Utils.dpToPx(8.0F, requireContext())))
            .into(binding.addPicture)
    }

    private fun saveImageToPrivateStorage(uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val filePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlist_image"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${UUID.randomUUID()}.jpg")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.absolutePath
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}