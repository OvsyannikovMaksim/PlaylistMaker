package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val editTextLayout = findViewById<TextInputLayout>(R.id.textField)
        val editText = findViewById<TextInputEditText>(R.id.edit_text)

        val textWatcher =
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                    TODO("Not yet implemented")
                }

                override fun onTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int,
                ) {
                    TODO("Not yet implemented")
                }

                override fun afterTextChanged(p0: Editable?) {
                    TODO("Not yet implemented")
                }
            }

        editTextLayout.setEndIconOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            editText.text?.clear()
            editText.clearFocus()
        }

        editText.addTextChangedListener(textWatcher)
    }
}
