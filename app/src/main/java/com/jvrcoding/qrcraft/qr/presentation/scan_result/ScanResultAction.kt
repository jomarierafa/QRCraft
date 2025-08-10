package com.jvrcoding.qrcraft.qr.presentation.scan_result

sealed interface ScanResultAction {
    data object OnShareButtonClick: ScanResultAction
    data object OnCopyButtonClick: ScanResultAction
    data object OnBackIconClick: ScanResultAction
}