package com.example.playlistmaker.data.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(string: String)

    fun openLink(string: String)

    fun openEmail(emailData: EmailData)
}
