package com.jvrcoding.qrcraft.app.navigation

import com.jvrcoding.qrcraft.qr.domain.scanner.QrType
import kotlinx.serialization.Serializable

sealed interface NavigationRoute {

    @Serializable
    object MainScreen

    @Serializable
    data object QRScanner: NavigationRoute

    @Serializable
    data object CreateQR: NavigationRoute

    @Serializable
    data class DataEntry(
        val qrType: QrType
    ): NavigationRoute

    @Serializable
    data class ScanResult(
        val qrCodeValue: String,
        val qrCodeRawValue: String,
        val qrType: QrType
    ): NavigationRoute

}