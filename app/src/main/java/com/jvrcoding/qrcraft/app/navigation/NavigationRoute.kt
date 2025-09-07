package com.jvrcoding.qrcraft.app.navigation

import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi
import kotlinx.serialization.Serializable

sealed interface NavigationRoute {

    @Serializable
    object MainScreen

    @Serializable
    data object QRScanner: NavigationRoute

    @Serializable
    data object CreateQR: NavigationRoute

    @Serializable
    data object History: NavigationRoute

    @Serializable
    data class DataEntry(
        val qrType: QrTypeUi
    ): NavigationRoute

    @Serializable
    data class PreviewScreen(
        val toolbarTitle: String,
        val qrId: String,
    ): NavigationRoute

}