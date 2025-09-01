package com.jvrcoding.qrcraft.qr.presentation.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HistoryViewModel: ViewModel() {

    var state by mutableStateOf(HistoryState())
        private set


    fun onAction(action: HistoryAction) {
        when(action) {
            is HistoryAction.ChangeTab -> {
                state = state.copy(
                    activeTab = action.tab
                )
            }
        }

    }
}