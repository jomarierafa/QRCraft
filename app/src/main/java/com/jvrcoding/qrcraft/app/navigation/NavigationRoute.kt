package com.jvrcoding.qrcraft.app.navigation

import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable
    data object QRScanner: NavigationRoute

    @Serializable
    data class ScanResult(
        val qrCodeValue: String,
        val qrType: Int
    ): NavigationRoute

}