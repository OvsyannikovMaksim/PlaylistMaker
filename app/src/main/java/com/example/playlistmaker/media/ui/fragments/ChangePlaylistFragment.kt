package com.example.playlistmaker.media.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.ui.view_model.ChangePlaylistViewModel
import com.example.playlistmaker.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ChangePlaylistFragment : Fragment() {

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!
    protected open val viewModel by viewModel<ChangePlaylistViewModel>()
    private val args: ChangePlaylistFragmentArgs by navArgs()
    private var uri: Uri? = null

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

        val pickImage = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            setPicture(uri)
            this.uri = uri
        }

        viewModel.getPlaylist(args.playlistId)

        viewModel.getPlaylist().observe(viewLifecycleOwner) {
            setUpUi(it)
        }

        binding.inputPlaylistName.addTextChangedListener(
            onTextChanged = { text: CharSequence?, _: Int, _: Int, _: Int ->
                binding.addPlaylistButton.isEnabled = !text.isNullOrEmpty()
            }
        )

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.addPicture.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.addPlaylistButton.setOnClickListener {
            val name = binding.inputPlaylistName.text.toString()
            val desc = binding.inputPlaylistDesc.text.toString()
            val imagePath = saveImageToPrivateStorage(uri)
            viewModel.savePlayList(name, desc, imagePath)
            findNavController().popBackStack()
        }
    }

    private fun setUpUi(playlist: Playlist) {
        binding.apply {
            inputPlaylistName.setText(playlist.name)
            inputPlaylistDesc.setText(playlist.desc)
            addPlaylistButton.setText(R.string.update_playlist)
            toolbar.title = ""
        }
        val imageUri = if (playlist.imagePath == null) {
            null
        } else {
            File(playlist.imagePath).toUri()
        }
        setPicture(imageUri)
    }

    private fun setPicture(uri: Uri?) {
        Glide.with(this)
            .load(uri)
            .centerCrop()
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