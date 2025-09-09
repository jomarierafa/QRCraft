package com.jvrcoding.qrcraft.qr.presentation.history.model

import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.util.UiText

enum class Tab(val title: UiText) {
    SCANNED(UiText.StringResource(R.string.scanned)),
    GENERATED(UiText.StringResource(R.string.generated))
}