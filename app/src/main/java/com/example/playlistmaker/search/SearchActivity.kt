package com.example.playlistmaker.search

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
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
    private lateinit var errorFoundRefreshButton: Button

    private var songs = arrayListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        savedInstanceState?.let { restoreTrackList(it) }

        toolbar = findViewById(R.id.search_toolbar)
        editText = findViewById(R.id.edit_text)
        clearSearchButton = findViewById(R.id.search_clear_button)
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        nothingFoundPlaceholder = findViewById(R.id.nothing_found_placeholder)
        errorFoundPlaceholder = findViewById(R.id.error_found_placeholder)
        errorFoundRefreshButton = findViewById(R.id.error_found_refresh_button)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        searchText?.let { editText.setText(it) }
        Log.d("TEST", "onCreate")
        trackRecyclerView.adapter = TrackListAdapter(songs)

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
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString().isEmpty()) {
                        clearSearchButton.visibility = View.GONE
                    } else {
                        clearSearchButton.visibility = View.VISIBLE
                    }
                }
            }

        clearSearchButton.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            editText.text?.clear()
            clearSearchButton.visibility = View.GONE
            songs.clear()
            trackRecyclerView.adapter?.notifyDataSetChanged()
        }

        editText.addTextChangedListener(textWatcher)

        editText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSong(textView.text.toString())
            }
            false
        }

        errorFoundRefreshButton.setOnClickListener {
            searchText?.let { searchSong(it) }
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

    fun restoreTrackList(savedInstanceState: Bundle) {
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
