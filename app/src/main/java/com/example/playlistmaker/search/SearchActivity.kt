package com.example.playlistmaker.search

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.TrackListAdapter
import com.example.playlistmaker.api.RetrofitITunes
import com.example.playlistmaker.api.SongResponse
import com.example.playlistmaker.api.Track
import com.example.playlistmaker.utils.SharedPreferences
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private var searchText: String? = null
    private lateinit var toolbar: Toolbar
    private lateinit var editText: EditText
    private lateinit var clearSearchButton: ImageView
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var nothingFoundPlaceholder: ViewGroup
    private lateinit var errorFoundPlaceholder: ViewGroup
    private lateinit var errorFoundRefreshButton: MaterialButton
    private lateinit var history: View
    private lateinit var clearHistoryButton: MaterialButton
    private lateinit var historyRecyclerView: RecyclerView

    private var songs = arrayListOf<Track>()
    private val historyList by lazy { SharedPreferences.getTrackHistory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        savedInstanceState?.let { restoreTrackList(it) }

        history = findViewById(R.id.history)
        toolbar = findViewById(R.id.search_toolbar)
        editText = findViewById(R.id.edit_text)
        clearSearchButton = findViewById(R.id.search_clear_button)
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        nothingFoundPlaceholder = findViewById(R.id.nothing_found_placeholder)
        errorFoundPlaceholder = findViewById(R.id.error_found_placeholder)
        errorFoundRefreshButton = findViewById(R.id.error_found_refresh_button)
        clearHistoryButton = findViewById(R.id.clear_history_button)
        historyRecyclerView = findViewById(R.id.history_rv)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        searchText?.let { editText.setText(it) }
        trackRecyclerView.adapter = TrackListAdapter(songs)
        historyRecyclerView.adapter = TrackListAdapter(historyList)
        val listener =
            OnSharedPreferenceChangeListener { _, key ->
                if (key == SharedPreferences.TRACKHISTORY) {
                    val tracks = SharedPreferences.getTrackHistory(this)
                    historyList.apply {
                        clear()
                        addAll(tracks)
                    }
                    historyRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

        SharedPreferences
            .getSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(listener)

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
                        editText.hasFocus() && p0?.isEmpty() == true && historyList.isNotEmpty()
                    trackRecyclerView.isVisible = !history.isVisible
                    Log.d("TEST", "$p0 $p1 $p2 $p3")
                }

                override fun afterTextChanged(p0: Editable?) {
                    clearSearchButton.isVisible = p0.toString().isNotEmpty()
                    if (p0.toString().isEmpty()) {
                        songs.clear()
                        trackRecyclerView.adapter?.notifyDataSetChanged()
                    }
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
        }

        editText.addTextChangedListener(textWatcher)
        editText.setOnFocusChangeListener { _, hasFocus ->
            history.isVisible = hasFocus && editText.text.isEmpty() && historyList.isNotEmpty()
            trackRecyclerView.isVisible = !history.isVisible
        }

        editText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSong(textView.text.toString())
            }
            false
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

    private fun searchSong(text: String) {
        RetrofitITunes.iTunesApi.search(text).enqueue(
            object : Callback<SongResponse> {
                override fun onResponse(
                    call: Call<SongResponse>,
                    response: Response<SongResponse>,
                ) {
                    if (response.code() == 200) {
                        trackRecyclerView.isVisible = true
                        errorFoundPlaceholder.isVisible = false
                        nothingFoundPlaceholder.isVisible = false
                        songs.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            songs.addAll(response.body()?.results!!)
                            trackRecyclerView.adapter?.notifyDataSetChanged()
                        } else {
                            trackRecyclerView.isVisible = false
                            errorFoundPlaceholder.isVisible = false
                            nothingFoundPlaceholder.isVisible = true
                        }
                    }
                }

                override fun onFailure(
                    call: Call<SongResponse>,
                    t: Throwable,
                ) {
                    trackRecyclerView.isVisible = false
                    nothingFoundPlaceholder.isVisible = false
                    errorFoundPlaceholder.isVisible = true
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

    companion object {
        const val EDIT_TEXT_TAG = "EditTextTag"
        const val TRACK_LIST_TAG = "TrackListTag"
    }
}
