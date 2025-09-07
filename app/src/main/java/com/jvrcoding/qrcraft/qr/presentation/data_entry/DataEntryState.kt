package com.jvrcoding.qrcraft.qr.presentation.data_entry

import androidx.compose.foundation.text.input.TextFieldState
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi

data class DataEntryState(
    val qrType: QrTypeUi = QrTypeUi.TEXT,

    val text: TextFieldState = TextFieldState(),
    val canGenerateTextQr: Boolean = false,

    val link: TextFieldState = TextFieldState(),
    val canGenerateLinkQr: Boolean = false,

    val name: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val phoneNumber: TextFieldState = TextFieldState(),
    val canGenerateContactQr: Boolean = false,
    val canGeneratePhoneQr: Boolean = false,

    val latitude: TextFieldState = TextFieldState(),
    val longitude: TextFieldState = TextFieldState(),
    val canGenerateGeoQr: Boolean = false,

    val wifiSsid: TextFieldState = TextFieldState(),
    val wifiPassword: TextFieldState = TextFieldState(),
    val wifiEncryption: TextFieldState = TextFieldState(),
    val canGenerateWifiQr: Boolean = false
)
