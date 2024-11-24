package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.SharedPreferences
import com.example.playlistmaker.domain.api.SongInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.TrackListAdapter
import com.example.playlistmaker.ui.audioplayer.AudioPlayerActivity
import com.example.playlistmaker.utils.Creator
import com.google.android.material.button.MaterialButton

@SuppressLint("NotifyDataSetChanged")
class SearchActivity : AppCompatActivity() {
    private var searchText: String? = null

    private lateinit var toolbar: Toolbar
    private lateinit var editText: EditText
    private lateinit var clearSearchButton: ImageView
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var placeholder: ViewGroup
    private lateinit var errorFoundRefreshButton: MaterialButton
    private lateinit var history: View
    private lateinit var clearHistoryButton: MaterialButton
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var progressBar: ProgressBar

    private var songs = arrayListOf<Track>()
    private val historyList by lazy { SharedPreferences.getTrackHistory(this) }
    private var listener: OnSharedPreferenceChangeListener? = null

    private lateinit var songInteractor: SongInteractor

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        savedInstanceState?.let { restoreTrackList(it) }

        history = findViewById(R.id.history)
        toolbar = findViewById(R.id.search_toolbar)
        editText = findViewById(R.id.edit_text)
        clearSearchButton = findViewById(R.id.search_clear_button)
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        placeholder = findViewById(R.id.placeholder)
        placeholderImage = findViewById(R.id.placeholder_image)
        placeholderText = findViewById(R.id.placeholder_text)
        errorFoundRefreshButton = findViewById(R.id.error_found_refresh_button)
        clearHistoryButton = findViewById(R.id.clear_history_button)
        historyRecyclerView = findViewById(R.id.history_rv)
        progressBar = findViewById(R.id.progress_bar)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        searchText?.let { editText.setText(it) }
        trackRecyclerView.adapter = TrackListAdapter(songs, clickListener)
        historyRecyclerView.adapter = TrackListAdapter(historyList, clickListener)
        listener =
            OnSharedPreferenceChangeListener { _, key ->
                if (key == SharedPreferences.getTrackHistoryTag()) {
                    val tracks = SharedPreferences.getTrackHistory(this)
                    historyList.apply {
                        clear()
                        addAll(tracks)
                    }
                    historyRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

        listener?.let { SharedPreferences.registerChangeListener(this, it) }

        songInteractor = Creator.getSongInteractor()

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
                    history.isVisible =
                        editText.hasFocus() &&
                        p0?.isEmpty() == true &&
                        historyList.isNotEmpty()
                    trackRecyclerView.isVisible = !history.isVisible
                    songs.clear()
                    trackRecyclerView.adapter?.notifyDataSetChanged()
                    progressBar.isVisible = p0.toString().isNotEmpty()
                    searchDebounce()
                }

                override fun afterTextChanged(p0: Editable?) {
                    clearSearchButton.isVisible = p0.toString().isNotEmpty()
                }
            }

        clearSearchButton.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            editText.text?.clear()
            clearSearchButton.isVisible = false
            songs.clear()
            trackRecyclerView.adapter?.notifyDataSetChanged()
            placeholder.isVisible = false
        }

        editText.addTextChangedListener(textWatcher)
        editText.setOnFocusChangeListener { _, hasFocus ->
            history.isVisible = hasFocus && editText.text.isEmpty() && historyList.isNotEmpty()
            trackRecyclerView.isVisible = !history.isVisible
        }

        errorFoundRefreshButton.setOnClickListener {
            searchText?.let { searchSong(it) }
        }

        clearHistoryButton.setOnClickListener {
            SharedPreferences.clearTrackHistory(this)
            history.isVisible = false
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

    private fun searchSong(text: String) {
        songInteractor.searchSong(
            text,
            object : SongInteractor.SongConsumer {
                override fun onSuccess(foundSongs: List<Track>) {
                    runOnUiThread {
                        progressBar.isVisible = false
                        songs.clear()
                        if (foundSongs.isNotEmpty()) {
                            trackRecyclerView.isVisible = true
                            songs.addAll(foundSongs)
                            trackRecyclerView.adapter?.notifyDataSetChanged()
                        } else {
                            trackRecyclerView.isVisible = false
                            showNothingFoundPlaceholder()
                        }
                    }
                }

                override fun onFailure(exception: Exception) {
                    runOnUiThread {
                        progressBar.isVisible = false
                        trackRecyclerView.isVisible = false
                        showErrorPlaceholder()
                    }
                }
            },
        )
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

    private fun showNothingFoundPlaceholder() {
        placeholderImage.setImageResource(R.drawable.nothing_found_pic)
        placeholderText.setText(R.string.nothing_found)
        errorFoundRefreshButton.isVisible = false
        placeholder.isVisible = true
    }

    private fun showErrorPlaceholder() {
        placeholderImage.setImageResource(R.drawable.error_found_pic)
        placeholderText.setText(R.string.error_found)
        errorFoundRefreshButton.isVisible = true
        placeholder.isVisible = true
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
            Runnable { if (editText.text.isNotEmpty()) searchSong(editText.text.toString()) }
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
