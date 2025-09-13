package com.jvrcoding.qrcraft.qr.presentation.history

import com.jvrcoding.qrcraft.qr.presentation.history.model.Tab
import com.jvrcoding.qrcraft.qr.presentation.models.QrUi

data class HistoryState(
    val activeTab: Tab = Tab.SCANNED,
    val scannedQrs: List<QrUi> = emptyList(),
    val generatedQrs: List<QrUi> = emptyList(),
    val selectedQr: QrUi? = null
)
