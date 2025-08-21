package com.jvrcoding.qrcraft.qr.domain.scanner

data class ScanResultDetail(
    val qrValue: String,
    val qrRawValue: String,
    val qrType: QrType,
)
