package com.jvrcoding.qrcraft.qr.presentation.scan_result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.util.UiText

class ScanResultViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var state by mutableStateOf(ScanResultState())
        private set

    private val qrValue: String = savedStateHandle["qrCodeValue"] ?: ""
    private val qrType: Int = savedStateHandle["qrType"] ?: Barcode.TYPE_TEXT

    init {
        setResult()
    }

    private fun setResult() {
        state = state.copy(
            contentType = qrType.toQrTypeText(),
            contentValue = qrValue
        )
    }

    fun Int.toQrTypeText(): UiText {
        return when(this) {
            Barcode.TYPE_GEO -> UiText.StringResource(R.string.link)
            Barcode.TYPE_URL -> UiText.StringResource(R.string.link)
            Barcode.TYPE_CONTACT_INFO -> UiText.StringResource(R.string.link)
            Barcode.TYPE_PHONE -> UiText.StringResource(R.string.link)
            Barcode.TYPE_WIFI -> UiText.StringResource(R.string.link)
            Barcode.TYPE_TEXT -> UiText.StringResource(R.string.link)
            else -> UiText.StringResource(R.string.link)
        }
    }

}