package com.jvrcoding.qrcraft.qr.data.qr_generator

import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.jvrcoding.qrcraft.qr.domain.qr_generator.QRCode
import com.jvrcoding.qrcraft.qr.domain.qr_generator.QrCodeGenerator

class ZxingQrCodeGenerator: QrCodeGenerator {
    override fun generate(
        content: String,
        size: Int
    ): QRCode {
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val pixels = Array(bitMatrix.width) { BooleanArray(bitMatrix.height) }

        for (x in 0 until bitMatrix.width) {
            for (y in 0 until bitMatrix.height) {
                pixels[x][y] = bitMatrix[x, y]
            }
        }

        return QRCode(
            width = bitMatrix.width,
            height = bitMatrix.height,
            pixels = pixels
        )
    }
}