package com.jvrcoding.qrcraft.qr.presentation.data_entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftToolbar
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.qr.domain.scanner.QrType
import com.jvrcoding.qrcraft.qr.presentation.data_entry.components.QRContactDataForm
import com.jvrcoding.qrcraft.qr.presentation.data_entry.components.QRGeoDataForm
import com.jvrcoding.qrcraft.qr.presentation.data_entry.components.QRLinkDataForm
import com.jvrcoding.qrcraft.qr.presentation.data_entry.components.QRPhoneDataForm
import com.jvrcoding.qrcraft.qr.presentation.data_entry.components.QRTextDataForm
import com.jvrcoding.qrcraft.qr.presentation.data_entry.components.QRWifiDataForm
import org.koin.androidx.compose.koinViewModel

@Composable
fun DataEntryScreenRoot(
    onBackClick: () -> Unit,
    viewModel: DataEntryViewModel = koinViewModel(),
) {
    DataScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                DataEntryAction.OnBackClick -> onBackClick()
            }
        }
    )
}

@Composable
fun DataScreen(
    state: DataEntryState,
    onAction: (DataEntryAction) -> Unit
) {
    Scaffold(
        topBar = {
            QRCraftToolbar(
                title = state.toolbarText.asString(),
                showBackButton = true,
                textColor = MaterialTheme.colorScheme.onSurface,
                onBackClick = { onAction(DataEntryAction.OnBackClick)}
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {

            when(state.qrType) {
                QrType.TEXT -> QRTextDataForm()
                QrType.LINK -> QRLinkDataForm()
                QrType.GEOLOCATION -> QRGeoDataForm()
                QrType.WIFI -> QRWifiDataForm()
                QrType.CONTACT -> QRContactDataForm()
                QrType.PHONE -> QRPhoneDataForm()
            }
        }
    }
}

@Preview
@Composable
private fun DataScreenPreview() {
    QRCraftTheme {
        DataScreen(
            state = DataEntryState(),
            onAction = {}
        )
    }
}