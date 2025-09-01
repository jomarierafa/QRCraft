package com.jvrcoding.qrcraft.qr.presentation.util

import com.jvrcoding.qrcraft.app.navigation.NavigationRoute
import com.jvrcoding.qrcraft.core.presentation.util.UiText
import com.jvrcoding.qrcraft.qr.domain.scanner.ScanResultDetail

fun ScanResultDetail.toPreviewScreenRoute(toolbarTitle: String): NavigationRoute.PreviewScreen {
    return NavigationRoute.PreviewScreen(
        toolbarTitle = toolbarTitle,
        qrCodeValue = this.qrValue,
        qrCodeRawValue = this.qrRawValue,
        qrType = this.qrType
    )
}

fun NavigationRoute.PreviewScreen.toScanResultDetail(): ScanResultDetail {
    return ScanResultDetail(
        qrValue = this.qrCodeValue,
        qrRawValue = this.qrCodeRawValue,
        qrType = this.qrType
    )
}