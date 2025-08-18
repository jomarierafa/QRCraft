package com.jvrcoding.qrcraft.qr.domain.scanner

import androidx.camera.core.ImageProxy

interface QrScannerRepository {
    suspend fun scan(imageProxy: ImageProxy): ScanResultDetail?
}