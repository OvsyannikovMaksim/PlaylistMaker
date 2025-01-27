package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(
    val context: Context,
) : ExternalNavigator {

    override fun openEmail(emailData: EmailData) {
        val intent =
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, emailData.email)
                putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
                putExtra(Intent.EXTRA_TEXT, emailData.body)
            }
        context.startActivity(intent)
    }

    override fun openLink(string: String) {
        val intent =
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(string)
            }
        context.startActivity(intent)
    }

    override fun shareLink(string: String) {
        val intent =
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, string)
                type = "text/plain"
            }
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.chooser_text)))
    }
}
