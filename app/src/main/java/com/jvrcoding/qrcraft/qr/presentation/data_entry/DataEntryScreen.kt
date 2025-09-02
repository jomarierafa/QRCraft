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
import com.jvrcoding.qrcraft.core.presentation.util.ObserveAsEvents
import com.jvrcoding.qrcraft.qr.domain.qr.QrType
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
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
    onNavigateToPreviewScreen: (QrDetail) -> Unit,
    viewModel: DataEntryViewModel = koinViewModel(),
) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when(event) {
            is DataEntryEvent.QrCodeGenerated -> {
                onNavigateToPreviewScreen(event.qrDetail)
            }
        }
    }
    DataScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                DataEntryAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
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
                QrType.TEXT -> QRTextDataForm(
                    text = state.text,
                    buttonEnable = state.canGenerateTextQr,
                    onButtonClick = { onAction(DataEntryAction.OnGenerateButtonClick) }
                )
                QrType.LINK -> QRLinkDataForm(
                    link = state.link,
                    buttonEnable = state.canGenerateLinkQr,
                    onButtonClick = { onAction(DataEntryAction.OnGenerateButtonClick) }
                )
                QrType.GEOLOCATION -> QRGeoDataForm(
                    latitude = state.latitude,
                    longitude = state.longitude,
                    buttonEnable = state.canGenerateGeoQr,
                    onButtonClick = { onAction(DataEntryAction.OnGenerateButtonClick) }
                )
                QrType.WIFI -> QRWifiDataForm(
                    ssid = state.wifiSsid,
                    password = state.wifiPassword,
                    encryption = state.wifiEncryption,
                    buttonEnable = state.canGenerateWifiQr,
                    onButtonClick = { onAction(DataEntryAction.OnGenerateButtonClick) }
                )
                QrType.CONTACT -> QRContactDataForm(
                    name = state.name,
                    email = state.email,
                    phoneNumber = state.phoneNumber,
                    buttonEnable = state.canGenerateContactQr,
                    onButtonClick = { onAction(DataEntryAction.OnGenerateButtonClick) }
                )
                QrType.PHONE -> QRPhoneDataForm(
                    phoneNumber = state.phoneNumber,
                    buttonEnable = state.canGeneratePhoneQr,
                    onButtonClick = { onAction(DataEntryAction.OnGenerateButtonClick) }
                )
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