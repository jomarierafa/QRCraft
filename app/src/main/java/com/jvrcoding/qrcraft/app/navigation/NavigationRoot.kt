package com.jvrcoding.qrcraft.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.QRSCannerScreenRoot

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
                onNavigateToScanResult = {
                    navController.navigate(NavigationRoute.ScanResult(""))
                }
            )
        }
        composable<NavigationRoute.ScanResult> {
        }
    }
}