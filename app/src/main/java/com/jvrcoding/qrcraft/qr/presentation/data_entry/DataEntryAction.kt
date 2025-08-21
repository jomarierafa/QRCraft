package com.jvrcoding.qrcraft.qr.presentation.data_entry

sealed interface DataEntryAction {
    data object OnBackClick: DataEntryAction
}