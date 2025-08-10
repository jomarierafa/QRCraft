package com.jvrcoding.qrcraft.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Success

@Composable
fun QRCraftSnackBar(
    message: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF323232),
) {
    Surface(
        modifier = modifier
            .padding(16.dp)
            .wrapContentWidth()
            .wrapContentHeight(),
        color = backgroundColor,
        shape = RoundedCornerShape(6.dp),
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier
                .background(color = Success)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = message,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

        }
    }
}

@Preview
@Composable
private fun QRCraftSnackBarPreview() {
    QRCraftTheme {
        QRCraftSnackBar(
            message = "Camera permission granted",
        )
    }
}