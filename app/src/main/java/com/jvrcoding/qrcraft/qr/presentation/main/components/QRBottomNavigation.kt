package com.jvrcoding.qrcraft.qr.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QrCraftIconButton
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.AddIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.RefreshIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.ScanIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.linkBG
import com.jvrcoding.qrcraft.qr.presentation.main.model.BottomNavItem

@Composable
fun QRBottomNavigation(
    selectedItemId: BottomNavItem,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = CircleShape
                )
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            QrCraftIconButton(
                onClick = { onItemClick(BottomNavItem.HISTORY) },
                containerColor = if(selectedItemId == BottomNavItem.HISTORY)
                    MaterialTheme.colorScheme.linkBG
                else Color.Transparent,
                icon = RefreshIcon,
                contentDescription = stringResource(R.string.history_tab),
                modifier = Modifier.size(44.dp)
            )

            Spacer(Modifier.width(70.dp))

            QrCraftIconButton(
                onClick = { onItemClick(BottomNavItem.CREATE_QR) },
                containerColor =  if(selectedItemId == BottomNavItem.CREATE_QR)
                    MaterialTheme.colorScheme.linkBG
                else Color.Transparent,
                icon = AddIcon,
                contentDescription = stringResource(R.string.generate_qr),
                modifier = Modifier.size(44.dp)
            )
        }

        QrCraftIconButton(
            onClick = { onItemClick(BottomNavItem.SCAN_QR) },
            containerColor =  MaterialTheme.colorScheme.primary,
            icon = ScanIcon,
            contentDescription = stringResource(R.string.scan_qr),
            iconSize = 28.dp,
            modifier = Modifier.size(64.dp)
        )
    }

}

@Preview
@Composable
private fun QRBottomNavigationPreview() {
    QRCraftTheme {
        QRBottomNavigation(
            selectedItemId = BottomNavItem.CREATE_QR,
            onItemClick = {}
        )
    }
}