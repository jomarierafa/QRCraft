package com.jvrcoding.qrcraft.qr.presentation.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvrcoding.qrcraft.qr.domain.qr.LocalQrDataSource
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
import com.jvrcoding.qrcraft.qr.domain.qr.Transaction
import com.jvrcoding.qrcraft.qr.presentation.history.model.Tab
import com.jvrcoding.qrcraft.qr.presentation.models.QrUi
import com.jvrcoding.qrcraft.qr.presentation.util.toQrUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val qrDataSource: LocalQrDataSource
): ViewModel() {

    var state by mutableStateOf(HistoryState())
        private set

    private val filteredQrs = qrDataSource
        .getQrList()
        .filterByTab()
        .onEach { qrs ->
            val qrsUi = qrs.map { it.toQrUi() }
            state = state.copy(qrList = qrsUi)
        }

    init {
        filteredQrs
            .launchIn(viewModelScope)
    }

    private fun Flow<List<QrDetail>>.filterByTab(): Flow<List<QrDetail>> {
        return combine(
            this,
            snapshotFlow { state.activeTab },
        ) { qrs, tab ->
            when(tab) {
                Tab.SCANNED -> qrs.filter { it.transactionType == Transaction.SCANNED }
                Tab.GENERATED -> qrs.filter {  it.transactionType == Transaction.GENERATED }
            }
        }
    }


    fun onAction(action: HistoryAction) {
        when(action) {
            is HistoryAction.ChangeTab -> changeTab(action.tab)
            is HistoryAction.OnItemClick -> {}
            is HistoryAction.OnItemLongClick -> onItemLongClick(action.qr)
            HistoryAction.OnDismissItemMenu -> onDismissItemMenu()
            is HistoryAction.OnDeleteQrClick -> deleteQr(action.qrId)
            else -> Unit
        }

    }

    private fun changeTab(tab: Tab) {
        state = state.copy(
            activeTab = tab
        )
    }

    private fun onDismissItemMenu() {
        state = state.copy(selectedQr = null)
    }

    private fun onItemLongClick(qr: QrUi) {
        state = state.copy(selectedQr = qr)
    }

    private fun deleteQr(qrId: String) {
        state = state.copy(selectedQr = null)
        viewModelScope.launch {
            qrDataSource.deleteQr(id = qrId)
        }
    }
}