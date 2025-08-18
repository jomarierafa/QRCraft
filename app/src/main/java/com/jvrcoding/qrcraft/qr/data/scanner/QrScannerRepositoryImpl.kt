package com.jvrcoding.qrcraft.qr.data.scanner

import android.graphics.Rect
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.jvrcoding.qrcraft.qr.domain.scanner.QrType
import com.jvrcoding.qrcraft.qr.domain.scanner.QrScannerRepository
import com.jvrcoding.qrcraft.qr.domain.scanner.ScanResultDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.math.min

@ExperimentalCoroutinesApi
class QrScannerRepositoryImpl() : QrScannerRepository {

    val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override suspend fun scan(imageProxy: ImageProxy): ScanResultDetail? {

        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return null
        }
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

        // compute upright dimensions
        val uprightWidth = if (rotationDegrees == 90 || rotationDegrees == 270) {
            imageProxy.height
        } else {
            imageProxy.width
        }
        val uprightHeight = if (rotationDegrees == 90 || rotationDegrees == 270) {
            imageProxy.width
        } else {
            imageProxy.height
        }

        val boxRatio = 0.5f
        val minDim = min(uprightWidth, uprightHeight)
        val boxSize = minDim * boxRatio
        val scanBox = Rect(
            ((uprightWidth - boxSize) / 2).toInt().coerceAtLeast(0),
            ((uprightHeight - boxSize) / 2).toInt().coerceAtLeast(0),
            ((uprightWidth + boxSize) / 2).toInt().coerceAtMost(uprightWidth),
            ((uprightHeight + boxSize) / 2).toInt().coerceAtMost(uprightHeight)
        )

        val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)

        return suspendCancellableCoroutine { cont ->
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    barcodes.firstOrNull {
                        it.boundingBox != null && scanBox.contains(it.boundingBox!!)
                    }?.let {
                        val qrValue = parseQr(it)
                        val format = getQrFormat(it.valueType)
                        Log.d("QrScannerRepository", "scan: $format")
                        cont.resume(
                            ScanResultDetail(qrValue, format),
                            null
                        )
                    }
                }
                .addOnFailureListener {
                    cont.resume(null, null)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun getQrFormat(type: Int): QrType {
        return when(type) {
            Barcode.TYPE_GEO -> QrType.GEOLOCATION
            Barcode.TYPE_URL -> QrType.LINK
            Barcode.TYPE_CONTACT_INFO -> QrType.CONTACT
            Barcode.TYPE_PHONE -> QrType.PHONE
            Barcode.TYPE_WIFI -> QrType.WIFI
            else -> QrType.TEXT
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
