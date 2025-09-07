package com.jvrcoding.qrcraft.qr.presentation.data_entry

import com.jvrcoding.qrcraft.qr.domain.qr.QrDetailId

sealed interface DataEntryEvent {
    data class QrCodeGenerated(
        val qrId: QrDetailId
    ): DataEntryEvent
}