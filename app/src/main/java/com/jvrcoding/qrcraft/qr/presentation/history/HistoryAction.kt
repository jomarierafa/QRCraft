package com.jvrcoding.qrcraft.qr.presentation.history

import com.jvrcoding.qrcraft.qr.presentation.history.model.Tab

sealed interface HistoryAction {
    data class ChangeTab(val tab: Tab) : HistoryAction
}