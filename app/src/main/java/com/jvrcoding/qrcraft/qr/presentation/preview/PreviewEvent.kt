package com.jvrcoding.qrcraft.qr.presentation.preview

sealed interface PreviewEvent {
    data class ShareData(val data: String): PreviewEvent
    data class CopyText(val data: String): PreviewEvent
}