package com.jvrcoding.qrcraft.qr.domain.scanner

import com.jvrcoding.qrcraft.qr.domain.qr.QrType

data class ScanResult(
    val qrValue: String,
    val qrRawValue: String,
    val qrType: QrType,
)
