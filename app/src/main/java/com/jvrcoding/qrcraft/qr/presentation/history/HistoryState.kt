package com.jvrcoding.qrcraft.qr.presentation.history

import com.jvrcoding.qrcraft.qr.presentation.history.model.Tab

data class HistoryState(
    val activeTab: Tab = Tab.SCANNED,
//    val items: List<Item> = emptyList()
)
