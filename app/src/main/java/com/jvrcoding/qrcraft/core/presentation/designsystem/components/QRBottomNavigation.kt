package com.jvrcoding.qrcraft.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.AddIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.LinkBG
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.RefreshIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.ScanIcon

@Composable
fun QRBottomNavigation(
    modifier: Modifier = Modifier
) {
    val selectedBackgroundColor = LinkBG
    val unselectedBackgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh
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
            IconButton(
                modifier = Modifier
                    .background(Color.White, CircleShape)
                    .size(44.dp),
                onClick = {}
            ) {
                Icon(
                    imageVector = RefreshIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(Modifier.width(70.dp))

            IconButton(
                modifier = Modifier
                    .background(selectedBackgroundColor, CircleShape)
                    .size(44.dp),
                onClick = {}
            ) {
                Icon(
                    imageVector = AddIcon,
                    contentDescription = stringResource(R.string.generate_qr),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        IconButton(
            modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .align(Alignment.Center),
            onClick = {}
        ) {
            Icon(
                imageVector = ScanIcon,
                contentDescription = stringResource(R.string.scan_qr),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(28.dp)
            )
        }
    }

}

@Preview
@Composable
private fun QRBottomNavigationPreview() {
    QRCraftTheme {
        QRBottomNavigation()
    }
}