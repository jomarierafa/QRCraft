package com.jvrcoding.qrcraft.qr.presentation.util

import android.graphics.Bitmap
import android.graphics.Color
import com.jvrcoding.qrcraft.qr.domain.qr_generator.QRCode
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

fun QRCode.toBitmap(): Bitmap {
    val bitmap = createBitmap(width, height)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap[x, y] = if (pixels[x][y]) Color.BLACK else Color.WHITE
        }
    }
    return bitmap
}