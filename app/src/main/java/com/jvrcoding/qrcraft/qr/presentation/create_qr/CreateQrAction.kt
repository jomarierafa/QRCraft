package com.jvrcoding.qrcraft.qr.presentation.create_qr

import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi

sealed interface CreateQrAction {
    data class OnItemClicked(val qrType: QrTypeUi) : CreateQrAction
}