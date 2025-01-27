package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.main.data.SharedPreferences
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.audioplayer.ui.AudioPlayerActivity

@SuppressLint("NotifyDataSetChanged")
class SearchActivity : AppCompatActivity() {
    private var searchText: String? = null
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel

    private var songs = arrayListOf<Track>()
    private var historyList = arrayListOf<Track>()
    private var listener: OnSharedPreferenceChangeListener? = null

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)
        savedInstanceState?.let { restoreTrackList(it) }

        setSupportActionBar(binding.searchToolbar)
        binding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        searchViewModel = ViewModelProvider(
            this,
            SearchViewModelFactory.getViewModelFactory(),
        )[SearchViewModel::class.java]

        searchViewModel.getScreenState().observe(this) {
            render(it)
        }

        searchViewModel.getHistoryList().observe(this) {
            historyList = it
            binding.historyRv.adapter?.notifyDataSetChanged()
        }

        binding.trackRecyclerView.adapter = TrackListAdapter(songs, clickListener)
        binding.historyRv.adapter = TrackListAdapter(historyList, clickListener)
        searchText?.let { binding.editText.setText(it) }


        val textWatcher =
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                ) {
                    searchText = p0.toString()
                    binding.history.isVisible =
                        binding.editText.hasFocus() &&
                                p0?.isEmpty() == true &&
                                historyList.isNotEmpty()
                    binding.trackRecyclerView.isVisible = !binding.history.isVisible
                    songs.clear()
                    binding.trackRecyclerView.adapter?.notifyDataSetChanged()
                    binding.progressBar.isVisible = p0.toString().isNotEmpty()
                    searchDebounce()
                }

                override fun afterTextChanged(p0: Editable?) {
                    binding.searchClearButton.isVisible = p0.toString().isNotEmpty()
                }
            }

        binding.searchClearButton.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            binding.editText.text?.clear()
            binding.searchClearButton.isVisible = false
            songs.clear()
            binding.trackRecyclerView.adapter?.notifyDataSetChanged()
            binding.placeholder.isVisible = false
        }

        binding.editText.addTextChangedListener(textWatcher)
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            binding.history.isVisible =
                hasFocus && binding.editText.text!!.isEmpty() && historyList.isNotEmpty()
            binding.trackRecyclerView.isVisible = !binding.history.isVisible
        }

        binding.errorFoundRefreshButton.setOnClickListener {
            searchText?.let { searchViewModel.searchSong(it) }
        }

        binding.clearHistoryButton.setOnClickListener {
            SharedPreferences.clearTrackHistory(this)
            binding.history.isVisible = false
        }
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

    override fun onDestroy() {
        super.onDestroy()
        listener?.let { SharedPreferences.unregisterChangeListener(this, it) }
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Success -> showTracks(state.trackList)
            is SearchScreenState.EmptyResult -> showNothingFoundPlaceholder()
            is SearchScreenState.Error -> showErrorPlaceholder()
        }

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

    private fun showTracks(tracks: List<Track>) {
        binding.progressBar.isVisible = false
        songs.clear()
        binding.trackRecyclerView.isVisible = true
        songs.addAll(tracks)
        binding.trackRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun showNothingFoundPlaceholder() {
        binding.progressBar.isVisible = false
        songs.clear()
        binding.placeholderImage.setImageResource(R.drawable.nothing_found_pic)
        binding.placeholderText.setText(R.string.nothing_found)
        binding.errorFoundRefreshButton.isVisible = false
        binding.placeholder.isVisible = true
    }

    private fun showErrorPlaceholder() {
        binding.placeholderImage.setImageResource(R.drawable.error_found_pic)
        binding.placeholderText.setText(R.string.error_found)
        binding.errorFoundRefreshButton.isVisible = true
        binding.placeholder.isVisible = true
    }

    private val clickListener =
        TrackListAdapter.TrackClickListener { track ->
            if (clickDebounce()) {
                SharedPreferences.putTrackToHistory(this@SearchActivity, track)
                AudioPlayerActivity.launch(this@SearchActivity, track)
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

    private fun searchDebounce() {
        val searchRunnable =
            Runnable { if (binding.editText.text!!.isNotEmpty()) searchViewModel.searchSong(binding.editText.text.toString()) }
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val EDIT_TEXT_TAG = "EditTextTag"
        const val TRACK_LIST_TAG = "TrackListTag"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
