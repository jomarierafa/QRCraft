package com.jvrcoding.qrcraft.qr.presentation.data_entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jvrcoding.qrcraft.qr.domain.scanner.QrType
import com.jvrcoding.qrcraft.qr.presentation.util.toQrTypeText

class DataEntryViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val qrType: QrType = savedStateHandle["qrType"] ?: QrType.TEXT

    var state by mutableStateOf(DataEntryState(
        qrType = qrType,
        toolbarText = qrType.toQrTypeText()
    ))
        private set


}