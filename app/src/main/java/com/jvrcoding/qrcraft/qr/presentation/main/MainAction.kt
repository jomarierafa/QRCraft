package com.jvrcoding.qrcraft.qr.presentation.main

import com.jvrcoding.qrcraft.qr.domain.qr.QrType
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetailId
import com.jvrcoding.qrcraft.qr.presentation.main.model.BottomNavItem

sealed interface MainAction {
    data object DismissRationaleDialog: MainAction
    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraRationale: Boolean
    ): MainAction
    data class OnBottomNavigationItemClick(val screenItem: BottomNavItem) : MainAction
    data class OnCreateQrItemClick(val qrType: QrType) : MainAction
    data class OnNavigateToPreviewScreen(val qrId: QrDetailId) : MainAction
}