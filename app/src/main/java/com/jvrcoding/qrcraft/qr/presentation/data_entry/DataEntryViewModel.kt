package com.jvrcoding.qrcraft.qr.presentation.data_entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.qrcraft.core.presentation.util.textAsFlow
import com.jvrcoding.qrcraft.qr.domain.qr.QrType
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
import com.jvrcoding.qrcraft.qr.presentation.util.toQrTypeText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class DataEntryViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val qrType: QrType = savedStateHandle["qrType"] ?: QrType.TEXT

    var state by mutableStateOf(DataEntryState(
        qrType = qrType,
        toolbarText = qrType.toQrTypeText()
    ))
        private set

    private val eventChannel = Channel<DataEntryEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        state.text.textAsFlow()
            .onEach { text ->
                state = state.copy(
                    canGenerateTextQr = text.isNotBlank()
                )
            }
            .launchIn(viewModelScope)

        state.link.textAsFlow()
            .onEach { link ->
                state = state.copy(
                    canGenerateLinkQr = link.isNotBlank()
                )
            }
            .launchIn(viewModelScope)

        state.phoneNumber.textAsFlow()
            .onEach { link ->
                state = state.copy(
                    canGeneratePhoneQr = link.isNotBlank()
                )
            }
            .launchIn(viewModelScope)

        combine(
            state.name.textAsFlow(),
            state.email.textAsFlow(),
            state.phoneNumber.textAsFlow()
        ) { name, email, phoneNumber ->
            state = state.copy(
                canGenerateContactQr = name.isNotBlank() && email.isNotBlank() && phoneNumber.isNotBlank()
            )
        }.launchIn(viewModelScope)

        combine(
            state.latitude.textAsFlow(),
            state.longitude.textAsFlow()
        ) { latitude, longitude ->
            state = state.copy(
                canGenerateGeoQr = latitude.isNotBlank() && longitude.isNotBlank()
            )
        }.launchIn(viewModelScope)

        combine(
            state.wifiSsid.textAsFlow(),
            state.wifiPassword.textAsFlow(),
            state.wifiEncryption.textAsFlow()
        ) { ssid, password, encryption ->
            state = state.copy(
                canGenerateWifiQr = ssid.isNotBlank() && password.isNotBlank() && encryption.isNotBlank()
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: DataEntryAction) {
        when(action) {
            DataEntryAction.OnGenerateButtonClick -> generateQr()
            else -> Unit
        }

    }

    private fun generateQr() {
        viewModelScope.launch {
            eventChannel.send(DataEntryEvent.QrCodeGenerated(
                qrDetail = QrDetail(
                    id = "",
                    qrValue = displayText(),
                    qrRawValue = rawValue(),
                    qrType = qrType,
                    createdAt = ZonedDateTime.now()
                )
            ))
        }
    }

    private fun rawValue(): String {
        return  when(qrType) {
            QrType.TEXT -> state.text.text.toString()
            QrType.LINK -> state.link.text.toString()
            QrType.GEOLOCATION -> "geo:${state.latitude.text},${state.longitude.text}"
            QrType.WIFI -> "WIFI:S${state.wifiSsid.text};T:${state.wifiEncryption.text};P:${state.wifiPassword.text};;"
            QrType.CONTACT -> """
                            BEGIN:VCARD
                            VERSION:3.0
                            N:${state.name.text}
                            TEL:${state.phoneNumber.text}
                            EMAIL:${state.email.text}
                            END:VCARD
                            """.trimIndent()
            QrType.PHONE -> "tel:${state.phoneNumber.text}"
        }
    }

    private fun displayText(): String {
        return when(qrType) {
            QrType.TEXT -> "${state.text.text}"
            QrType.LINK -> "${state.link.text}"
            QrType.GEOLOCATION -> "${state.latitude.text}, ${state.longitude.text}"
            QrType.WIFI -> "SSID: ${state.wifiSsid.text}\n" +
                    "Password: ${state.wifiPassword.text}\n" +
                    "Encryption type: ${state.wifiEncryption.text}"
            QrType.CONTACT -> "${state.name.text}\n" +
                    "${state.email.text}\n" +
                    "${state.phoneNumber.text}"
            QrType.PHONE -> "${state.phoneNumber.text}"
        }
    }


}