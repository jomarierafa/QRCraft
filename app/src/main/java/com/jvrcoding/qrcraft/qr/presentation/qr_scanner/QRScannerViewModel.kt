package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.QRScannerEvent.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class QRScannerViewModel(
): ViewModel() {

    var state by mutableStateOf(QRScannerState())
        private set

    private val eventChannel = Channel<QRScannerEvent>()
    val events = eventChannel.receiveAsFlow()


    fun onAction(action: QRScannerAction) {
        when(action) {
            is QRScannerAction.SubmitCameraPermissionInfo -> {
                state = state.copy(
                    hasCameraPermission = action.acceptedCameraPermission,
                    showCameraRationale = action.showCameraRationale
                )
            }

            QRScannerAction.DismissRationaleDialog -> {
                state = state.copy(
                    showCameraRationale = false
                )
            }

            is QRScannerAction.OnSuccessfulScan -> {
                state = state.copy(
                    isQRProcessing = true
                )
                viewModelScope.launch {
                    delay(500)
                    eventChannel.send(
                        ScanResult(
                            type = action.barcode.valueType,
                            qrValue = parseQr(action.barcode)
                        )
                    )

                    delay(500)
                    state = state.copy(
                        isQRProcessing = false
                    )
                }
            }
        }
    }



    private fun parseQr(barcode: Barcode): String {
        return when (barcode.valueType) {
            Barcode.TYPE_GEO -> "${barcode.geoPoint!!.lng}, ${barcode.geoPoint!!.lng}"
            Barcode.TYPE_URL -> barcode.url?.url.orEmpty()
            Barcode.TYPE_CONTACT_INFO -> {
                val contact = barcode.contactInfo
                "${contact?.name?.formattedName}\n" +
                        "${contact?.emails?.firstOrNull()?.address}\n" +
                        "${contact?.phones?.firstOrNull()?.number}"
            }

            Barcode.TYPE_PHONE -> {
                "${barcode.phone?.number}"
            }

            Barcode.TYPE_WIFI -> {
                val wifi = barcode.wifi
                val encryption = when (wifi?.encryptionType) {
                    Barcode.WiFi.TYPE_OPEN -> "Open"
                    Barcode.WiFi.TYPE_WEP -> "WEP"
                    Barcode.WiFi.TYPE_WPA -> "WPA"
                    else -> null
                }
                "SSID: ${wifi?.ssid ?: ""}\n" +
                        "Password: ${wifi?.password ?: ""}\n" +
                        "Encryption type: $encryption"
            }

            Barcode.TYPE_TEXT -> barcode.rawValue.orEmpty()
            else -> barcode.rawValue.orEmpty()
        }
    }


}