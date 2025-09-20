package com.jvrcoding.qrcraft.qr.domain.scanner

import android.net.Uri
import androidx.camera.core.ImageProxy

interface QrScanner {
    suspend fun scan(imageProxy: ImageProxy): ScanResult?
    suspend fun scanQrFromUri(uri: Uri): ScanResult?
}