package com.jvrcoding.qrcraft.qr.presentation.main


sealed interface MainEvent {
    data object NavigateToScanQr: MainEvent
    data object NavigateToHistory: MainEvent
    data object NavigateToCreateQr: MainEvent
}