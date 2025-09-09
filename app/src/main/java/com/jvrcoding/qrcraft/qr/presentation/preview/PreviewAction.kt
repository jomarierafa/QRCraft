package com.jvrcoding.qrcraft.qr.presentation.preview

sealed interface PreviewAction {
    data object OnShareButtonClick: PreviewAction
    data object OnCopyButtonClick: PreviewAction
    data object OnBackIconClick: PreviewAction
    data class OnTitleTextChange(val text: String): PreviewAction
}