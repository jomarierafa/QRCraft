package com.jvrcoding.qrcraft.core.presentation.designsystem.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.onOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCraftToolbar(
    title: String,
    showBackButton:  Boolean,
    actionIcon:@Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    textColor: Color = MaterialTheme.colorScheme.onOverlay,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(color = textColor),
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            if(showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.go_back),
                        tint = textColor
                    )
                }
            }
        },
        actions = actionIcon
    )
}

@Preview
@Composable
private fun QRCraftToolbarPreview() {
    QRCraftTheme {
        QRCraftToolbar(
            title = "Scan Result",
            showBackButton = true,
            onBackClick = {}
        )
    }
}
