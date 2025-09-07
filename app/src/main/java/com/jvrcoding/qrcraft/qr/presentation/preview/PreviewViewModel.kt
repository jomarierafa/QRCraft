package com.jvrcoding.qrcraft.qr.presentation.preview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.qrcraft.qr.domain.qr.LocalQrDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.jvrcoding.qrcraft.qr.domain.qr_generator.QrCodeGenerator
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi
import com.jvrcoding.qrcraft.qr.presentation.util.toBitmap

class PreviewViewModel(
    savedStateHandle: SavedStateHandle,
    private val qrCodeGenerator: QrCodeGenerator,
    private val qrDataSource: LocalQrDataSource
): ViewModel() {

    private val toolbarTitle: String = savedStateHandle["toolbarTitle"] ?:""
    private val qrId: String = savedStateHandle["qrId"] ?: ""

    var state by mutableStateOf(PreviewState(
        toolbarTitle = toolbarTitle,
    ))
        private set

    init {
        getQrDetails()
    }

    private val eventChannel = Channel<PreviewEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: PreviewAction) {
        when(action) {
            PreviewAction.OnCopyButtonClick -> {
                viewModelScope.launch {
                    eventChannel.send(PreviewEvent.CopyText(state.contentValue))
                }
            }
            PreviewAction.OnShareButtonClick -> {
                viewModelScope.launch {
                    eventChannel.send(PreviewEvent.ShareData(state.contentValue))
                }
            }
            else -> Unit
        }
    }

    private fun getQrDetails() {
        viewModelScope.launch {
            val qrDetails = qrDataSource.getQr(id = qrId) ?: throw IllegalStateException("Qr Details must not be null")
            val qrImage = qrCodeGenerator.generate(qrDetails.qrRawValue, 500)
            state = state.copy(
                qrImage = qrImage.toBitmap(),
                qrType = QrTypeUi.valueOf(qrDetails.qrType.name),
                contentValue = qrDetails.qrValue
            )
        }
    }

}