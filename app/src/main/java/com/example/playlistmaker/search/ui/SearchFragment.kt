package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.ui.AudioPlayerActivity
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.model.SearchScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.adapter.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("NotifyDataSetChanged")
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()

    private var searchText: String? = null
    private var songs = arrayListOf<Track>()
    private var historyList = arrayListOf<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            searchText = it.getString(EDIT_TEXT_TAG)
        }

        binding.errorFoundRefreshButton.setOnClickListener {
            viewModel.setState(SearchScreenState.InProgress)
            searchText?.let { viewModel.searchDebounce(it) }
        }

        binding.clearHistoryButton.setOnClickListener {
            hideHistory()
            viewModel.clearHistory()
        }

        viewModel.getScreenState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.searchClearButton.setOnClickListener {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
            binding.editText.text?.clear()
            songs.clear()
            binding.trackRv.adapter?.notifyDataSetChanged()
        }

        binding.editText.addTextChangedListener(
            beforeTextChanged = { _: CharSequence?, _: Int, _: Int, _: Int -> },
            onTextChanged = { p0: CharSequence?, _: Int, _: Int, _: Int ->
                if (p0.isNullOrEmpty()) {
                    viewModel.removeCallbacks()
                    binding.searchClearButton.isVisible = false
                    val history = viewModel.getHistory()
                    if (binding.editText.hasFocus() && history.isNotEmpty()) {
                        viewModel.setState(SearchScreenState.History(history))
                    } else {
                        viewModel.setState(SearchScreenState.Nothing)
                    }
                } else {
                    binding.searchClearButton.isVisible = true
                    searchText = p0.toString()
                    searchText?.let { viewModel.searchDebounce(it) }
                }
            },
            afterTextChanged = { _: Editable? -> },
        )

        binding.trackRv.adapter = TrackListAdapter(songs, clickListener)
        binding.historyRv.adapter = TrackListAdapter(historyList, clickListener)
        searchText?.let { binding.editText.setText(it) }
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            val history = viewModel.getHistory()
            if(hasFocus && history.isNotEmpty()) {
                viewModel.setState(SearchScreenState.History(history))
            } else {
                viewModel.setState(SearchScreenState.Nothing)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchText?.let { outState.putString(EDIT_TEXT_TAG, it) }
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.InProgress -> showLoading()
            is SearchScreenState.SuccessSearch -> showTracks(state.tracks)
            is SearchScreenState.EmptySearch -> showNothingFoundPlaceholder()
            is SearchScreenState.ErrorSearch -> showErrorPlaceholder()
            is SearchScreenState.History -> showHistory(state.tracks)
            is SearchScreenState.Nothing -> {
                hideHistory()
                hideTracks()
                hidePlaceholders()
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        hideHistory()
        hideTracks()
        hidePlaceholders()
    }

    private fun showTracks(tracks: ArrayList<Track>) {
        binding.trackRv.isVisible = true
        binding.progressBar.isVisible = false
        binding.history.isVisible = false
        binding.placeholder.isVisible = false
        songs.clear()
        songs.addAll(tracks)
        binding.trackRv.adapter?.notifyDataSetChanged()
    }

    private fun showHistory(tracks: ArrayList<Track>) {
        hideLoading()
        hidePlaceholders()
        hideTracks()
        binding.history.isVisible = true
        historyList.clear()
        historyList.addAll(tracks)
        binding.historyRv.adapter?.notifyDataSetChanged()
    }

    private fun showNothingFoundPlaceholder() {
        hideLoading()
        binding.placeholderImage.setImageResource(R.drawable.nothing_found_pic)
        binding.placeholderText.setText(R.string.nothing_found)
        binding.errorFoundRefreshButton.isVisible = false
        binding.placeholder.isVisible = true
    }

    private fun showErrorPlaceholder() {
        hideLoading()
        binding.placeholderImage.setImageResource(R.drawable.error_found_pic)
        binding.placeholderText.setText(R.string.error_found)
        binding.errorFoundRefreshButton.isVisible = true
        binding.placeholder.isVisible = true
    }

    private fun hideLoading() {
        binding.progressBar.isVisible = false
    }

    private fun hideHistory() {
        binding.history.isVisible = false
    }

    private fun hideTracks() {
        binding.trackRv.isVisible = false
        binding.searchClearButton.isVisible = false
    }

    private fun hidePlaceholders() {
        binding.placeholder.isVisible = false
    }

    private val clickListener =
        TrackListAdapter.TrackClickListener { track ->
            if (clickDebounce()) {
                viewModel.addHistory(track)
                AudioPlayerActivity.launch(requireActivity(), track)
            }
        }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val EDIT_TEXT_TAG = "EditTextTag"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}