package com.jvrcoding.qrcraft.qr.domain.scanner

import android.net.Uri
import androidx.camera.core.ImageProxy
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail

interface QrScanner {
    suspend fun scan(imageProxy: ImageProxy): QrDetail?
    suspend fun scanQrFromUri(uri: Uri): QrDetail?
}