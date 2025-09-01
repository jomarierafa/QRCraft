package com.jvrcoding.qrcraft.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jvrcoding.qrcraft.qr.presentation.data_entry.DataEntryScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.main.MainScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.preview.ScanResultScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.util.toPreviewScreenRoute

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
                    navController.navigate(
                        scanResultDetail.toPreviewScreenRoute("Scan Result")
                    )
                }
            )
        }

        composable<NavigationRoute.DataEntry> {
            DataEntryScreenRoot(
                onNavigateToPreviewScreen = { dataEntry ->
                    navController.navigate(
                        dataEntry.toPreviewScreenRoute("Preview")
                    )
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
        composable<NavigationRoute.PreviewScreen> {
            ScanResultScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}