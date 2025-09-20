package com.jvrcoding.qrcraft.qr.presentation.data_entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.qrcraft.core.presentation.util.textAsFlow
import com.jvrcoding.qrcraft.qr.domain.qr.LocalQrDataSource
import com.jvrcoding.qrcraft.qr.domain.qr.QrType
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
import com.jvrcoding.qrcraft.qr.domain.qr.Transaction
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi
import com.jvrcoding.qrcraft.qr.presentation.util.toTitleText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.UUID

class DataEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val qrDataSource: LocalQrDataSource
): ViewModel() {

    private val qrType: QrTypeUi = savedStateHandle["qrType"] ?: QrTypeUi.TEXT

    var state by mutableStateOf(DataEntryState(
        qrType = QrTypeUi.valueOf(qrType.name)
    ))
        private set

    private val eventChannel = Channel<DataEntryEvent>()
    val events = eventChannel.receiveAsFlow()

    private val qrId: String = UUID.randomUUID().toString()

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
            val qrDetail = QrDetail(
                id = qrId,
                qrTitleText = qrType.toTitleText(),
                qrValue = displayText(),
                qrRawValue = rawValue(),
                qrType = QrType.valueOf(qrType.name),
                isFavorite = false,
                transactionType = Transaction.GENERATED,
                createdAt = ZonedDateTime.now()
            )
            qrDataSource.upsertQr(qr = qrDetail)
            eventChannel.send(DataEntryEvent.QrCodeGenerated(qrId = qrId))
        }
    }

    private fun rawValue(): String {
        return when(qrType) {
            QrTypeUi.TEXT -> state.text.text.toString()
            QrTypeUi.LINK -> state.link.text.toString()
            QrTypeUi.GEOLOCATION -> "geo:${state.latitude.text},${state.longitude.text}"
            QrTypeUi.WIFI -> "WIFI:S:${state.wifiSsid.text};T:${state.wifiEncryption.text};P:${state.wifiPassword.text};;"
            QrTypeUi.CONTACT -> """
                            BEGIN:VCARD
                            VERSION:3.0
                            N:${state.name.text}
                            TEL:${state.phoneNumber.text}
                            EMAIL:${state.email.text}
                            END:VCARD
                            """.trimIndent()
            QrTypeUi.PHONE -> "tel:${state.phoneNumber.text}"
        }
    }

    private fun displayText(): String {
        return when(qrType) {
            QrTypeUi.TEXT -> "${state.text.text}"
            QrTypeUi.LINK -> "${state.link.text}"
            QrTypeUi.GEOLOCATION -> "${state.latitude.text}, ${state.longitude.text}"
            QrTypeUi.WIFI -> "SSID: ${state.wifiSsid.text}\n" +
                    "Password: ${state.wifiPassword.text}\n" +
                    "Encryption type: ${state.wifiEncryption.text}"
            QrTypeUi.CONTACT -> "${state.name.text}\n" +
                    "${state.email.text}\n" +
                    "${state.phoneNumber.text}"
            QrTypeUi.PHONE -> "${state.phoneNumber.text}"
        }
    }


}