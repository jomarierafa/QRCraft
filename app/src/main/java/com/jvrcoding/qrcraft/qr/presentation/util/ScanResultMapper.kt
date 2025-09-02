package com.jvrcoding.qrcraft.qr.presentation.util

import com.jvrcoding.qrcraft.app.navigation.NavigationRoute
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
import java.time.ZonedDateTime

fun QrDetail.toPreviewScreenRoute(toolbarTitle: String): NavigationRoute.PreviewScreen {
    return NavigationRoute.PreviewScreen(
        toolbarTitle = toolbarTitle,
        qrCodeValue = this.qrValue,
        qrCodeRawValue = this.qrRawValue,
        qrType = this.qrType
    )
}

fun NavigationRoute.PreviewScreen.toQrDetail(): QrDetail {
    return QrDetail(
        id = "",
        qrValue = this.qrCodeValue,
        qrRawValue = this.qrCodeRawValue,
        qrType = this.qrType,
        createdAt = ZonedDateTime.now()
    )
}