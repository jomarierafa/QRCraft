package com.jvrcoding.qrcraft.app.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable
    data object QRScanner: NavigationRoute

    @Serializable
    data class ScanResult(val qrCodeValue: String): NavigationRoute

}
