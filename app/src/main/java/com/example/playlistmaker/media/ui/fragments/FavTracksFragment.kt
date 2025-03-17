package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.databinding.FragmentFavTracksBinding
import com.example.playlistmaker.media.ui.view_model.FavTracksViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment: Fragment() {

    private var _binding: FragmentFavTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavTracksViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun newInstance() = FavTracksFragment()
    }
}