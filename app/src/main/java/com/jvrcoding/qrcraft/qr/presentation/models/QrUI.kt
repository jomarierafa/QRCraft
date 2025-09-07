package com.jvrcoding.qrcraft.qr.presentation.models

import com.jvrcoding.qrcraft.core.presentation.util.UiText

data class QrUi(
    val id: String,
    val title: UiText,
    val content: String,
    val date: String,
    val icon: Int
)