package com.jvrcoding.qrcraft.qr.presentation.data_entry

import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.util.UiText
import com.jvrcoding.qrcraft.qr.domain.scanner.QrType

data class DataEntryState(
    val toolbarText: UiText = UiText.StringResource(R.string.text),
    val qrType: QrType = QrType.TEXT,
)
