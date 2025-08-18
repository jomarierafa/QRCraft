package com.jvrcoding.qrcraft.app.navigation

import com.jvrcoding.qrcraft.qr.domain.scanner.QrType
import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable
    data object QRScanner: NavigationRoute

    @Serializable
    data class ScanResult(
        val qrCodeValue: String,
        val qrType: QrType
    ): NavigationRoute

}