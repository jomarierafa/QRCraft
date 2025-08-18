package com.jvrcoding.qrcraft.qr.presentation.util

import com.jvrcoding.qrcraft.app.navigation.NavigationRoute
import com.jvrcoding.qrcraft.qr.domain.scanner.ScanResultDetail

fun ScanResultDetail.toScanResultRoute(): NavigationRoute.ScanResult {
    return NavigationRoute.ScanResult(
        qrCodeValue = this.qrValue,
        qrType = this.qrType
    )
}

fun NavigationRoute.ScanResult.toScanResult(): ScanResultDetail {
    return ScanResultDetail(
        qrValue = this.qrCodeValue,
        qrType = this.qrType
    )
}