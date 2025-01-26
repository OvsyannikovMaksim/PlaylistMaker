package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    private fun getShareAppLink() = "https://practicum.yandex.ru/android-developer"

    private fun getSupportEmailData() =
        EmailData(
            "maksimov99997@yandex.ru",
            "Message to the developers of the Playlist Maker app",
            "Thanks to the developers for the cool app!",
        )

    private fun getTermsLink() = "https://yandex.ru/legal/practicum_offer/"
}
