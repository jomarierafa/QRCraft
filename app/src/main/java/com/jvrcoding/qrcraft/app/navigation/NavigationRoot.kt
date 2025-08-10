package com.jvrcoding.qrcraft.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.QRSCannerScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.scan_result.ScanResultScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.QRScanner,
    ) {
        composable<NavigationRoute.QRScanner> {
            QRSCannerScreenRoot(
                onNavigateToScanResult = { qrValue ->
                    navController.navigate(
                        NavigationRoute.ScanResult(
                            qrCodeValue = qrValue,
                            qrType = 1
                        )
                    )
                }
            )
        }
        composable<NavigationRoute.ScanResult> {
            ScanResultScreenRoot()
        }
    }
}