package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {
    private var text: String? = null
    private lateinit var toolbar: Toolbar
    private lateinit var editText: EditText
    private lateinit var clearSearchButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        toolbar = findViewById(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        editText = findViewById(R.id.edit_text)
        clearSearchButton = findViewById(R.id.search_clear_button)
        text?.let { editText.setText(it) }

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
                    text = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString().isEmpty())
                        {
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
        }

        editText.addTextChangedListener(textWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        text.let { outState.putString(EDIT_TEXT_TAG, it) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString(EDIT_TEXT_TAG)
    }

    companion object {
        const val EDIT_TEXT_TAG = "EditTextTag"
    }
}
