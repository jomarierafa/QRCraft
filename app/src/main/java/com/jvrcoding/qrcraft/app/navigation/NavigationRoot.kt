package com.jvrcoding.qrcraft.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jvrcoding.qrcraft.qr.presentation.data_entry.DataEntryScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.main.MainScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.preview.PreviewScreenRoot

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
                onNavigateToPreviewScreen = { qrId ->
                    navController.navigate(
                        NavigationRoute.PreviewScreen(
                            toolbarTitle = "Scan Result",
                            qrId = qrId
                        )
                    )
                }
            )
        }

        composable<NavigationRoute.DataEntry> {
            DataEntryScreenRoot(
                onNavigateToPreviewScreen = { qrId ->
                    navController.navigate(
                        NavigationRoute.PreviewScreen(
                            toolbarTitle = "Preview",
                            qrId = qrId
                        )
                    )
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
        composable<NavigationRoute.PreviewScreen> {
            PreviewScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}