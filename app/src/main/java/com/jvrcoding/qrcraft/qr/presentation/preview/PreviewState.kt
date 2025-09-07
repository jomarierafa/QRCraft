package com.jvrcoding.qrcraft.qr.presentation.preview

import android.graphics.Bitmap
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.util.UiText
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi

data class PreviewState(
    val toolbarTitle: String = "",
    val qrImage: Bitmap? = null,
    val qrType: QrTypeUi = QrTypeUi.TEXT,
    val title: UiText = UiText.StringResource(R.string.text),
    val contentValue: String = ""
)