package com.jvrcoding.qrcraft.qr.presentation.create_qr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftToolbar
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.util.DeviceLayoutType
import com.jvrcoding.qrcraft.core.presentation.util.rememberDeviceLayoutType
import com.jvrcoding.qrcraft.qr.presentation.create_qr.components.GridItem
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi


@Composable
fun CreateQrScreenRoot(
    onItemClick: (QrTypeUi) -> Unit,
) {
    CreateQrScreen(
        onAction = { action ->
            when (action) {
                is CreateQrAction.OnItemClicked -> onItemClick(action.qrType)
            }
        }
    )
}

@Composable
fun CreateQrScreen(
    onAction: (CreateQrAction) -> Unit,
) {
    Scaffold(
        topBar = {
            QRCraftToolbar(
                title = "Create QR",
                textColor = MaterialTheme.colorScheme.onSurface,
                showBackButton = false,
            )
        }
    ) { innerPadding ->
        val layoutType = rememberDeviceLayoutType()
        val columns = if (layoutType == DeviceLayoutType.PORTRAIT) 2 else 3
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(QrTypeUi.entries.toList()) { item ->
                GridItem(
                    text = item.title.asString(),
                    iconRes = item.icon,
                    tint = item.iconColor,
                    onClick = {
                        onAction(CreateQrAction.OnItemClicked(item))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateQrScreenPreview() {
    QRCraftTheme {
        CreateQrScreen(
            onAction = {}
        )
    }
}