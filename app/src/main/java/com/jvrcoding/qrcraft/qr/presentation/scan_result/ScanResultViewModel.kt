package com.jvrcoding.qrcraft.qr.presentation.scan_result

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.jvrcoding.qrcraft.qr.domain.scanner.QrType
import com.jvrcoding.qrcraft.qr.presentation.util.toQrTypeText

class ScanResultViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val qrValue: String = savedStateHandle["qrCodeValue"] ?: ""
    private val qrType: QrType = savedStateHandle["qrType"] ?: QrType.TEXT

    var state by mutableStateOf(ScanResultState(
        qrImage = generateQrCodeBitmap(qrValue, 500),
        contentTypeId = qrType,
        contentType = qrType.toQrTypeText(),
        contentValue = qrValue
    ))
        private set

    private val eventChannel = Channel<ScanResultEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ScanResultAction) {
        when(action) {
            ScanResultAction.OnCopyButtonClick -> {
                viewModelScope.launch {
                    eventChannel.send(ScanResultEvent.CopyText(qrValue))
                }
            }
            ScanResultAction.OnShareButtonClick -> {
                viewModelScope.launch {
                    eventChannel.send(ScanResultEvent.ShareData(qrValue))
                }
            }
            else -> Unit
        }
    }

    fun generateQrCodeBitmap(content: String, sizePx: Int): Bitmap? {
        try {
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = createBitmap(width, height)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap[x, y] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                }
            }
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}