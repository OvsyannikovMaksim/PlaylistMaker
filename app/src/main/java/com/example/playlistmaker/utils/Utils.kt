package com.example.playlistmaker.utils

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun dpToPx(
        dp: Float,
        context: Context,
    ): Int =
        TypedValue
            .applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics,
            ).toInt()

    fun timeConverter(time: Long?): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}
