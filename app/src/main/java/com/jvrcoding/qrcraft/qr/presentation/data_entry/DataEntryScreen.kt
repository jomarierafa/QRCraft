package com.jvrcoding.qrcraft.qr.presentation.data_entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftToolbar
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.qr.presentation.data_entry.components.QRTextDataForm

@Composable
fun DataEntryScreenRoot() {
    DataScreen()
}

@Composable
fun DataScreen() {
    Scaffold(
        topBar = {
            QRCraftToolbar(
                title = "Text",
                showBackButton = true,
                textColor = MaterialTheme.colorScheme.onSurface,
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

            QRTextDataForm(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .widthIn(max = 480.dp)
            )
        }
    }
}

@Preview
@Composable
private fun DataScreenPreview() {
    QRCraftTheme {
        DataScreen()
    }
}