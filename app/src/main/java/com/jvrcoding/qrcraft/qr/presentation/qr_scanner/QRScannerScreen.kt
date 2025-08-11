package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftDialog
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftSnackBar
import com.jvrcoding.qrcraft.core.presentation.util.ObserveAsEvents
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.components.CameraPreview
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.components.QRScannerOverlay
import com.jvrcoding.qrcraft.qr.presentation.util.hasCameraPermission
import com.jvrcoding.qrcraft.qr.presentation.util.shouldShowCameraPermissionRationale
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun QRSCannerScreenRoot(
    onNavigateToScanResult: (String, Int) -> Unit,
    viewModel: QRScannerViewModel = koinViewModel(),
){

    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when(event) {
            is QRScannerEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is QRScannerEvent.ScanResult -> {
                onNavigateToScanResult(
                    event.qrValue,
                    event.type
                )
            }
        }
    }

    QRScannerScreen(
        state = viewModel.state,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun QRScannerScreen(
    state: QRScannerState,
    onAction: (QRScannerAction) -> Unit
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
            QRScannerAction.SubmitCameraPermissionInfo(
                acceptedCameraPermission = hasCameraPermission,
                showCameraRationale = showCameraRationale
            )
        )
    }

    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showCameraRationale = activity.shouldShowCameraPermissionRationale()

        onAction(
            QRScannerAction.SubmitCameraPermissionInfo(
                acceptedCameraPermission = context.hasCameraPermission(),
                showCameraRationale = showCameraRationale
            )
        )

        if(!showCameraRationale) {
            permissionLauncher.requestQRCraftPermissions(context)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    QRCraftSnackBar(
                        message = data.visuals.message
                    )
                }
            )
        }
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.hasCameraPermission) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onScanResult = { barcode ->
                        onAction(QRScannerAction.OnSuccessfulScan(barcode))
                    }
                )
                QRScannerOverlay(state.isQRProcessing)
            } else {
                // Permission not accepted, and rationale is not currently shown (dialog will handle rationale)
                if (!state.showCameraRationale) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Camera permission is required to scan QR codes. Please grant the permission when prompted, or enable it in app settings if you have previously denied it.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
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
                                onAction(QRScannerAction.DismissRationaleDialog)
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
                                onAction(QRScannerAction.DismissRationaleDialog)
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

}

private fun ActivityResultLauncher<Array<String>>.requestQRCraftPermissions(
    context: Context
)
{
    val hasCameraPermission = context.hasCameraPermission()
    val cameraPermission = arrayOf(
        Manifest.permission.CAMERA,
    )

    when {
        !hasCameraPermission -> launch(cameraPermission)
    }
}
