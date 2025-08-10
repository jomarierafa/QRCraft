package com.jvrcoding.qrcraft.qr.presentation.qr_scanner.camera

import android.graphics.Rect
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlin.math.min

class QRAnalyzer(
    private val onResult: (Barcode) -> Unit,
    private val imageAnalysis: ImageAnalysis
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return imageProxy.close()

        val uprightWidth: Int
        val uprightHeight: Int
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

        if (rotationDegrees == 90 || rotationDegrees == 270) {
            uprightWidth = imageProxy.height
            uprightHeight = imageProxy.width
        } else {
            uprightWidth = imageProxy.width
            uprightHeight = imageProxy.height
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
        image

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull { it.boundingBox != null && scanBox.contains(it.boundingBox!!) }
                    ?.let {
                        Log.d("QRAnalyzer", "Barcode value ${it.rawValue}")
                        onResult(it)
                        imageAnalysis.clearAnalyzer()
                    }
            }
            .addOnFailureListener {
                Log.e("QRAnalyzer", "Scan failed", it)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}