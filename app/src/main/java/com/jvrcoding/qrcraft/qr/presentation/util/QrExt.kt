package com.jvrcoding.qrcraft.qr.presentation.util

import android.graphics.Bitmap
import android.graphics.Color
import com.jvrcoding.qrcraft.qr.domain.qr_generator.QRCode
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi

fun QRCode.toBitmap(): Bitmap {
    val bitmap = createBitmap(width, height)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap[x, y] = if (pixels[x][y]) Color.BLACK else Color.WHITE
        }
    }
    return bitmap
}

fun QrTypeUi.toTitleText(): String {
    return when(this) {
        QrTypeUi.TEXT -> "Text"
        QrTypeUi.LINK -> "Link"
        QrTypeUi.GEOLOCATION -> "Geolocation"
        QrTypeUi.WIFI -> "Wi-Fi"
        QrTypeUi.CONTACT -> "Contact"
        QrTypeUi.PHONE -> "Phone Number"
    }

}