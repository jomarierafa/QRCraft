package com.jvrcoding.qrcraft.qr.presentation.preview

import android.graphics.Bitmap
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi

data class PreviewState(
    val toolbarTitle: String = "",
    val qrImage: Bitmap? = null,
    val qrType: QrTypeUi = QrTypeUi.TEXT,
    val title: String = "",
    val contentValue: String = "",
    val isFavorite: Boolean = false
)