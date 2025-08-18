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
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.util.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.jvrcoding.qrcraft.qr.domain.scanner.QrType

class ScanResultViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var state by mutableStateOf(ScanResultState())
        private set

    private val eventChannel = Channel<ScanResultEvent>()
    val events = eventChannel.receiveAsFlow()

    private val qrValue: String = savedStateHandle["qrCodeValue"] ?: ""
    private val qrType: QrType = savedStateHandle["qrType"] ?: QrType.TEXT

    init {
        setResult()
    }

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

    private fun setResult() {
        state = state.copy(
            qrImage = generateQrCodeBitmap(qrValue, 500),
            contentTypeId = qrType,
            contentType = qrType.toQrTypeText(),
            contentValue = qrValue
        )
    }

    fun QrType.toQrTypeText(): UiText {
        return when(this) {
            QrType.GEOLOCATION -> UiText.StringResource(R.string.geolocation)
            QrType.LINK -> UiText.StringResource(R.string.link)
            QrType.CONTACT -> UiText.StringResource(R.string.contact)
            QrType.PHONE -> UiText.StringResource(R.string.phone_number)
            QrType.WIFI -> UiText.StringResource(R.string.wifi)
            else -> UiText.StringResource(R.string.text)
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