package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.qrcraft.qr.domain.scanner.QrScannerRepository
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.QRScannerEvent.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class QRScannerViewModel(
    private val qrScannerRepository: QrScannerRepository
): ViewModel() {

    var state by mutableStateOf(QRScannerState())
        private set

    private val eventChannel = Channel<QRScannerEvent>()
    val events = eventChannel.receiveAsFlow()


    fun onAction(action: QRScannerAction) {
        when(action) {
            is QRScannerAction.OnProcessImage -> {
                processImage(action.imageProxy, action.imageAnalysis)
            }

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

    private fun processImage(imageProxy: ImageProxy, imageAnalysis: ImageAnalysis) {
        viewModelScope.launch {
            val result = qrScannerRepository.scan(imageProxy)
            if (result != null && !state.isQRProcessing) {
                imageAnalysis.clearAnalyzer()
                state = state.copy(isQRProcessing = true)

                delay(500)
                eventChannel.send(
                    ScanResult(scanResultDetail = result)
                )

                delay(500)
                state = state.copy(isQRProcessing = false)
            }
        }
    }

}