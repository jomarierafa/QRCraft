package com.jvrcoding.qrcraft.qr.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.qrcraft.qr.presentation.main.model.BottomNavItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    private val eventChannel = Channel<MainEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: MainAction) {
        when(action) {
            is MainAction.OnBottomNavigationItemClick -> {
                if(state.selectedScreenItem == action.screenItem) return
                viewModelScope.launch {
                    state = state.copy(selectedScreenItem = action.screenItem)
                    eventChannel.send(
                        when(action.screenItem) {
                            BottomNavItem.SCAN_QR -> MainEvent.NavigateToScanQr
                            BottomNavItem.HISTORY -> MainEvent.NavigateToHistory
                            BottomNavItem.CREATE_QR -> MainEvent.NavigateToCreateQr

                        }
                    )
                }
            }
            is MainAction.SubmitCameraPermissionInfo -> {
                state = state.copy(
                    hasCameraPermission = action.acceptedCameraPermission,
                    showCameraRationale = action.showCameraRationale
                )

            }

            MainAction.DismissRationaleDialog -> {
                state = state.copy(
                    showCameraRationale = false
                )
            }

            else -> Unit
        }
    }
}