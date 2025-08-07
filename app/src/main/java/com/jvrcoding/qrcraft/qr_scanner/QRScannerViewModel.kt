package com.jvrcoding.qrcraft.qr_scanner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class QRScannerViewModel(
): ViewModel() {

    var state by mutableStateOf(QRScannerState())
        private set


    fun onAction(action: QRScannerAction) {
        when(action) {
            is QRScannerAction.SubmitCameraPermissionInfo -> {
                state = state.copy(
                    hasCameraPermission = action.acceptedCameraPermission,
                    showCameraRationale = action.showCameraRationale
                )
            }

            QRScannerAction.DismissRationaleDialog -> {
                state = state.copy(
                    showCameraRationale = false
                )
            }
        }
    }

}