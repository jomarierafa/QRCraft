package com.jvrcoding.qrcraft.qr.presentation.scan_result

import android.graphics.Bitmap
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.util.UiText
import com.jvrcoding.qrcraft.qr.domain.scanner.QrType

data class ScanResultState(
    val qrImage: Bitmap? = null,
    val contentTypeId: QrType = QrType.TEXT,
    val contentType: UiText = UiText.StringResource(R.string.text),
    val contentValue: String = ""
)