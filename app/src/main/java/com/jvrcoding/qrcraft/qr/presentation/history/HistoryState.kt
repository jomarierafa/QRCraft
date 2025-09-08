package com.jvrcoding.qrcraft.qr.presentation.history

import com.jvrcoding.qrcraft.qr.presentation.history.model.Tab
import com.jvrcoding.qrcraft.qr.presentation.models.QrUi

data class HistoryState(
    val activeTab: Tab = Tab.SCANNED,
    val qrList: List<QrUi> = emptyList(),
    val selectedQr: QrUi? = null
)
