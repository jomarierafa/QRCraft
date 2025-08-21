package com.jvrcoding.qrcraft.qr.presentation.create_qr

import com.jvrcoding.qrcraft.qr.domain.scanner.QrType

sealed interface CreateQrAction {
    data class OnItemClicked(val qrType: QrType) : CreateQrAction
}