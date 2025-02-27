package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(string: String)

    fun openLink(string: String)

    fun openEmail(emailData: EmailData)
}
