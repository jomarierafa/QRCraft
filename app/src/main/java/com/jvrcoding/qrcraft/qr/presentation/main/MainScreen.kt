package com.jvrcoding.qrcraft.qr.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.app.navigation.NavigationRoute
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftDialog
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftSnackBar
import com.jvrcoding.qrcraft.qr.presentation.main.components.QRBottomNavigation
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.util.ObserveAsEvents
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetailId
import com.jvrcoding.qrcraft.qr.presentation.create_qr.CreateQrScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.history.HistoryScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.QRSCannerScreenRoot
import com.jvrcoding.qrcraft.qr.presentation.util.hasCameraPermission
import com.jvrcoding.qrcraft.qr.presentation.util.shouldShowCameraPermissionRationale
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreenRoot(
    onCreateQrItemClicked: (QrTypeUi) -> Unit,
    onNavigateToPreviewScreen: (QrDetailId) -> Unit,
    viewModel: MainViewModel = koinViewModel(),
) {
    val bottomNavController = rememberNavController()

    ObserveAsEvents(flow = viewModel.events) { event ->
        when(event) {
            MainEvent.NavigateToScanQr -> {
                bottomNavController.navigate(NavigationRoute.QRScanner)
            }
            MainEvent.NavigateToHistory -> {
                bottomNavController.navigate(NavigationRoute.History)
            }
            MainEvent.NavigateToCreateQr -> {
                bottomNavController.navigate(NavigationRoute.CreateQR)
            }
        }
    }
    MainScreen(
        navController = bottomNavController,
        state = viewModel.state,
        onAction = { action ->
            when(action) {
                is MainAction.OnCreateQrItemClick ->  onCreateQrItemClicked(action.qrType)
                is MainAction.OnNavigateToPreviewScreen -> { onNavigateToPreviewScreen(action.qrId) }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
    state: MainState,
    onAction: (MainAction) -> Unit,
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasCameraPermission = perms[Manifest.permission.CAMERA] == true
        val activity = context as ComponentActivity
        val showCameraRationale = activity.shouldShowCameraPermissionRationale()

        if(hasCameraPermission) {
            coroutineScope.launch {
                snackBarHostState.showSnackbar(
                    message = context.getString(R.string.camera_permission_granted),
                    withDismissAction = false,
                    duration = SnackbarDuration.Short
                )

            }
        }

        onAction(
            MainAction.SubmitCameraPermissionInfo(
                acceptedCameraPermission = hasCameraPermission,
                showCameraRationale = showCameraRationale
            )
        )
    }

    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showCameraRationale = activity.shouldShowCameraPermissionRationale()

        onAction(
            MainAction.SubmitCameraPermissionInfo(
                acceptedCameraPermission = context.hasCameraPermission(),
                showCameraRationale = showCameraRationale
            )
        )

        if(!showCameraRationale) {
            permissionLauncher.requestQRCraftPermissions(context)
        }
    }

    Scaffold (
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    QRCraftSnackBar(
                        message = data.visuals.message
                    )
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                QRBottomNavigation(
                    selectedItemId = state.selectedScreenItem,
                    onItemClick = { item ->
                        onAction(MainAction.OnBottomNavigationItemClick(item))
                    },
                )
            }
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.QRScanner,
        ) {
            composable<NavigationRoute.QRScanner> {
                if(state.hasCameraPermission) {
                    QRSCannerScreenRoot(
                        onNavigateToPreviewScreen = { qrId ->
                            onAction(MainAction.OnNavigateToPreviewScreen(qrId))
                        },
                    )
                }
            }
            composable<NavigationRoute.CreateQR> {
                CreateQrScreenRoot(
                    onItemClick = { qrType ->
                        onAction(MainAction.OnCreateQrItemClick(qrType))
                    }
                )
            }
            composable<NavigationRoute.History> {
                HistoryScreenRoot(
                    onNavigateToPreviewScreen = {
                        onAction(MainAction.OnNavigateToPreviewScreen(it))
                    }
                )
            }
        }


        if (state.showCameraRationale) {
            QRCraftDialog(
                title = stringResource(R.string.camera_required),
                description = stringResource(R.string.rationale_dialog_message),
                onDismiss = { },
                primaryButton = {
                    Button(
                        onClick = {
                            onAction(MainAction.DismissRationaleDialog)
                            permissionLauncher.requestQRCraftPermissions(context)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                        shape = RoundedCornerShape(100f),
                        modifier = Modifier.weight(1f)
                    ) {
                        BasicText(
                            text = stringResource(R.string.grant_access),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            autoSize = TextAutoSize.StepBased(
                                minFontSize = 8.sp,
                                maxFontSize = 16.sp,
                            ),
                            maxLines = 1,
                        )
                    }
                },
                secondaryButton = {
                    Button(
                        onClick = {
                            onAction(MainAction.DismissRationaleDialog)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                        shape = RoundedCornerShape(100f),
                        modifier = Modifier.weight(1f)
                    ) {
                        BasicText(
                            text = stringResource(R.string.close_app),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.error
                            ),
                            autoSize = TextAutoSize.StepBased(
                                minFontSize = 8.sp,
                                maxFontSize = 16.sp,
                            ),
                            maxLines = 1,
                        )
                    }
                }
            )
        }
    }
}

private fun ActivityResultLauncher<Array<String>>.requestQRCraftPermissions(
    context: Context
) {
    val hasCameraPermission = context.hasCameraPermission()
    val cameraPermission = arrayOf(
        Manifest.permission.CAMERA,
    )

    when {
        !hasCameraPermission -> launch(cameraPermission)
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    QRCraftTheme {
        MainScreen(
            navController = rememberNavController(),
            state = MainState(),
            onAction = {}
        )
    }
}

