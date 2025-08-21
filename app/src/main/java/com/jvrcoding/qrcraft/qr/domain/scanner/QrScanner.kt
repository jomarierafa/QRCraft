package com.jvrcoding.qrcraft.qr.domain.scanner

import androidx.camera.core.ImageProxy

interface QrScanner {
    suspend fun scan(imageProxy: ImageProxy): ScanResultDetail?
}