package com.jvrcoding.qrcraft.qr.domain.qr_generator

interface QrCodeGenerator {
    fun generate(content: String, size: Int): QRCode
}