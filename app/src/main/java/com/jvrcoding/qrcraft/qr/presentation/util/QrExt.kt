package com.jvrcoding.qrcraft.qr.presentation.util

import android.graphics.Bitmap
import android.graphics.Color
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.util.UiText
import com.jvrcoding.qrcraft.qr.domain.qr_generator.QRCode
import com.jvrcoding.qrcraft.qr.domain.qr.QrType
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

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

fun QRCode.toBitmap(): Bitmap {
    val bitmap = createBitmap(width, height)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap[x, y] = if (pixels[x][y]) Color.BLACK else Color.WHITE
        }
    }
    return bitmap
}