package com.example.playlistmaker.utils

import android.content.Context
import android.util.TypedValue

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
}
