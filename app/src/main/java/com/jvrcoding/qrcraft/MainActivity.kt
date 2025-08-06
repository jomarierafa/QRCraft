package com.jvrcoding.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jvrcoding.qrcraft.qr_scanner.QRSCannerScreenRoot
import com.jvrcoding.qrcraft.qr_scanner.QRScannerViewModel
import com.jvrcoding.qrcraft.ui.theme.QRCraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            QRCraftTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    QRSCannerScreenRoot()

                }
            }
        }
    }
}
