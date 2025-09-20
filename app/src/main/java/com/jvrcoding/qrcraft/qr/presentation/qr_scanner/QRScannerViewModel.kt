package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import android.net.Uri
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.qrcraft.qr.domain.qr.LocalQrDataSource
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
import com.jvrcoding.qrcraft.qr.domain.qr.Transaction
import com.jvrcoding.qrcraft.qr.domain.scanner.QrScanner
import com.jvrcoding.qrcraft.qr.domain.scanner.ScanResult
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi
import com.jvrcoding.qrcraft.qr.presentation.util.toTitleText
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.UUID

class QRScannerViewModel(
    private val qrScanner: QrScanner,
    private val qrDataSource: LocalQrDataSource
): ViewModel() {

    var state by mutableStateOf(QRScannerState())
        private set

    private val eventChannel = Channel<QRScannerEvent>()
    val events = eventChannel.receiveAsFlow()


    private var dismissJob: Job? = null

    fun onAction(action: QRScannerAction) {
        when(action) {
            is QRScannerAction.OnProcessImage -> processImage(
                action.imageProxy,
                action.imageAnalysis
            )
            is QRScannerAction.OnImagePick -> scanQrFromImage(action.uri)
            QRScannerAction.ToggleTorch -> toggleTorch()
            QRScannerAction.OnDismissErrorDialog -> onDismissErrorDialog()
        }
    }

    private fun onDismissErrorDialog() {
        state = state.copy(showErrorDialog = false)
    }

    private fun toggleTorch() {
        state = state.copy(isTorchOn = !state.isTorchOn)
    }

    private suspend fun processResult(result: ScanResult) {
        val qrId = UUID.randomUUID().toString()
        val qrType = result.qrType
        val qrDetails = QrDetail(
            id =  qrId,
            qrTitleText = QrTypeUi.valueOf(qrType.name).toTitleText(),
            qrValue = result.qrValue,
            qrRawValue = result.qrRawValue,
            qrType = qrType,
            isFavorite = false,
            transactionType = Transaction.SCANNED,
            createdAt = ZonedDateTime.now()
        )
        qrDataSource.upsertQr(qr = qrDetails)

        delay(500)
        eventChannel.send(
            QRScannerEvent.SuccessfulScan(qrId = qrId)
        )

        delay(500)
        state = state.copy(isQRProcessing = false)
    }

    private fun scanQrFromImage(uri: Uri) {
        viewModelScope.launch {
            state = state.copy(
                isTorchOn = false,
                isQRProcessing = true
            )
            val result = qrScanner.scanQrFromUri(uri)
            if (result != null) {
               processResult(result)
            } else {
                delay(500)
                state = state.copy(
                    isQRProcessing = false,
                    showErrorDialog = true
                )
                startDismissTimer()
            }
        }
    }

    private fun processImage(imageProxy: ImageProxy, imageAnalysis: ImageAnalysis) {
        viewModelScope.launch {
            val result = qrScanner.scan(imageProxy)
            if (result != null && !state.isQRProcessing) {
//                imageAnalysis.clearAnalyzer()
                state = state.copy(
                    isTorchOn = false,
                    isQRProcessing = true
                )
               processResult(result)
            }
        }
    }

    private fun startDismissTimer() {
        cancelHideJob()
        dismissJob = viewModelScope.launch {
            delay(3000)
            state = state.copy(showErrorDialog = false)
        }
    }

    private fun cancelHideJob() {
        dismissJob?.cancel()
        dismissJob = null
    }

}