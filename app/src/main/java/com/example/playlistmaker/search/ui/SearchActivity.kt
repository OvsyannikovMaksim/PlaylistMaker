package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.ui.AudioPlayerActivity
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.model.SearchScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.adapter.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("NotifyDataSetChanged")
class SearchActivity : AppCompatActivity() {
    private var searchText: String? = null
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()

    private var songs = arrayListOf<Track>()
    private var historyList = arrayListOf<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)
        savedInstanceState?.let { restoreTrackList(it) }

        setSupportActionBar(binding.searchToolbar)
        binding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.errorFoundRefreshButton.setOnClickListener {
            viewModel.setState(SearchScreenState.InProgress)
            searchText?.let { viewModel.searchDebounce(it) }
        }

        binding.clearHistoryButton.setOnClickListener {
            hideHistory()
            viewModel.clearHistory()
        }

        viewModel.getScreenState().observe(this) {
            Log.d("STATE", it.toString())
            render(it)
        }

        binding.searchClearButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
                    val history = viewModel.getHistory()
                    if (binding.editText.hasFocus() && history.isNotEmpty()) {
                        viewModel.setState(SearchScreenState.History(history))
                    } else {
                        viewModel.setState(SearchScreenState.Nothing)
                    }
                } else {
                    searchText = p0.toString()
                    searchText?.let { viewModel.searchDebounce(it) }
                }
            },
            afterTextChanged = { _: Editable? -> },
        )

        binding.trackRv.adapter = TrackListAdapter(songs, clickListener)
        binding.historyRv.adapter = TrackListAdapter(historyList, clickListener)
        searchText?.let { binding.editText.setText(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchText?.let { outState.putString(EDIT_TEXT_TAG, it) }
        outState.putSerializable(TRACK_LIST_TAG, songs)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(EDIT_TEXT_TAG)
    }

    private fun restoreTrackList(savedInstanceState: Bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState
                .getSerializable(TRACK_LIST_TAG, ArrayList<Track>()::class.java)
                ?.let { songs = it }
        } else {
            savedInstanceState
                .getSerializable(TRACK_LIST_TAG)
                ?.let { songs = it as ArrayList<Track> }
        }
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
        binding.searchClearButton.isVisible = true
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
        binding.searchClearButton.isVisible = true
        hideLoading()
        binding.placeholderImage.setImageResource(R.drawable.nothing_found_pic)
        binding.placeholderText.setText(R.string.nothing_found)
        binding.errorFoundRefreshButton.isVisible = false
        binding.placeholder.isVisible = true
    }

    private fun showErrorPlaceholder() {
        binding.searchClearButton.isVisible = true
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
                AudioPlayerActivity.launch(this, track)
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
        const val TRACK_LIST_TAG = "TrackListTag"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
