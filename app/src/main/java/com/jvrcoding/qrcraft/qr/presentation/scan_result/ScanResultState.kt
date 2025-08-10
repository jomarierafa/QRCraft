package com.jvrcoding.qrcraft.qr.presentation.scan_result

import android.graphics.Bitmap
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.util.UiText

data class ScanResultState(
    val qrBitmap: Bitmap? = null,
    val contentType: UiText = UiText.StringResource(R.string.text),
    val contentValue: String = ""
)