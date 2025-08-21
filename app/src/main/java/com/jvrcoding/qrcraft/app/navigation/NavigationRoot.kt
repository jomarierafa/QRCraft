package com.jvrcoding.qrcraft.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jvrcoding.qrcraft.qr.presentation.data_entry.DataEntryScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.main.MainScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.scan_result.ScanResultScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.util.toScanResultRoute

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.MainScreen
    ) {
        composable<NavigationRoute.MainScreen> {
            MainScreenRoot(
                onCreateQrItemClicked = { qrType ->
                    navController.navigate(NavigationRoute.DataEntry(qrType))
                },
                onNavigateToScanResult = {  scanResultDetail ->
                    navController.navigate(scanResultDetail.toScanResultRoute())
                }
            )
        }

        composable<NavigationRoute.DataEntry> {
            DataEntryScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
        composable<NavigationRoute.ScanResult> {
            ScanResultScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}