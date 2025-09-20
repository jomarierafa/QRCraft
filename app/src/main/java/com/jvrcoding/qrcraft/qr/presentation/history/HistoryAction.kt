package com.jvrcoding.qrcraft.qr.presentation.history

import com.jvrcoding.qrcraft.qr.domain.qr.QrDetailId
import com.jvrcoding.qrcraft.qr.presentation.history.model.Tab
import com.jvrcoding.qrcraft.qr.presentation.models.QrUi

sealed interface HistoryAction {
    data class ChangeTab(val tab: Tab) : HistoryAction
    data class OnItemLongClick(val qr: QrUi) : HistoryAction
    data class OnItemClick(val qrId: QrDetailId): HistoryAction
    data class OnShareClick(val qrContent: String): HistoryAction
    data class OnDeleteQrClick(val qrId: QrDetailId): HistoryAction
    data class OnFavoriteClick(
        val qrId: QrDetailId,
        val isFavorite: Boolean
    ): HistoryAction
    data object OnDismissItemMenu: HistoryAction
}