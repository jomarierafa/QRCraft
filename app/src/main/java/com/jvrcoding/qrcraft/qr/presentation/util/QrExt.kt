package com.jvrcoding.qrcraft.qr.presentation.util

import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.util.UiText
import com.jvrcoding.qrcraft.qr.domain.scanner.QrType

fun QrType.toQrTypeText(): UiText {
    return when(this) {
        QrType.GEOLOCATION -> UiText.StringResource(R.string.geolocation)
        QrType.LINK -> UiText.StringResource(R.string.link)
        QrType.CONTACT -> UiText.StringResource(R.string.contact)
        QrType.PHONE -> UiText.StringResource(R.string.phone_number)
        QrType.WIFI -> UiText.StringResource(R.string.wifi)
        else -> UiText.StringResource(R.string.text)
    }
}