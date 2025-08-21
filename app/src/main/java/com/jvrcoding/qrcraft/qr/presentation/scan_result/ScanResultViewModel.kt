package com.jvrcoding.qrcraft.qr.presentation.scan_result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.jvrcoding.qrcraft.qr.domain.qr_generator.QrCodeGenerator
import com.jvrcoding.qrcraft.qr.domain.scanner.QrType
import com.jvrcoding.qrcraft.qr.presentation.util.toBitmap
import com.jvrcoding.qrcraft.qr.presentation.util.toQrTypeText

class ScanResultViewModel(
    savedStateHandle: SavedStateHandle,
    qrCodeGenerator: QrCodeGenerator
): ViewModel() {

    private val qrValue: String = savedStateHandle["qrCodeValue"] ?: ""
    private val qrRawValue: String = savedStateHandle["qrCodeRawValue"] ?: ""
    private val qrType: QrType = savedStateHandle["qrType"] ?: QrType.TEXT

    var state by mutableStateOf(ScanResultState(
        qrImage = qrCodeGenerator.generate(qrRawValue, 500).toBitmap(),
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

}