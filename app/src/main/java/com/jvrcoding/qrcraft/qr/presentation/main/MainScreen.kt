package com.jvrcoding.qrcraft.qr.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRBottomNavigation
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme

@Composable
fun MainScreenRoot() {
    MainScreen()
}

@Composable
fun MainScreen() {
    Scaffold (
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                QRBottomNavigation()
            }
        }
    ){ innerPadding ->

    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    QRCraftTheme {
        MainScreen()
    }
}