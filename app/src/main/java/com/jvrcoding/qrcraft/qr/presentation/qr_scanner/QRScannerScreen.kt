package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QrCraftIconButton
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.ImageIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.LightningIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.LightningOffIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.util.ObserveAsEvents
import com.jvrcoding.qrcraft.core.presentation.util.isAppInForeground
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetailId
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.components.CameraPreview
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.components.QRScannerOverlay
import org.koin.androidx.compose.koinViewModel


@Composable
fun QRSCannerScreenRoot(
    onNavigateToPreviewScreen: (QrDetailId) -> Unit,
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

            is QRScannerEvent.SuccessfulScan -> {
                onNavigateToPreviewScreen(event.qrId)
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
    var camera by remember { mutableStateOf<Camera?>(null) }

    val isAppInForeground by isAppInForeground()
    LaunchedEffect(state.isTorchOn, isAppInForeground) {
        if(state.isTorchOn && isAppInForeground) {
            camera?.cameraControl?.enableTorch(true)
        } else {
            camera?.cameraControl?.enableTorch(false)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onAction(QRScannerAction.OnImagePick(uri))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onAction = onAction,
            onCameraReady = { cam ->
                camera = cam
            }
        )
        QRScannerOverlay(state.isQRProcessing)

        if (!state.isQRProcessing) {
            QrCraftIconButton(
                onClick = { onAction(QRScannerAction.ToggleTorch) },
                containerColor = if(state.isTorchOn)
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceContainerHigh,
                icon = if(state.isTorchOn)
                    LightningOffIcon
                else LightningIcon,
                contentDescription = "lights on",
                modifier = Modifier
                    .padding(vertical = 64.dp, horizontal = 32.dp)
                    .size(44.dp)
            )

            QrCraftIconButton(
                onClick = { galleryLauncher.launch("image/*") },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                icon = ImageIcon,
                contentDescription = "lights on",
                modifier = Modifier
                    .padding(vertical = 64.dp, horizontal = 32.dp)
                    .align(Alignment.TopEnd)
                    .size(44.dp)

            )
        }

//        if (state.hasCameraPermission) {
//            CameraPreview(
//                modifier = Modifier.fillMaxSize(),
//                onAction = onAction
//            )
//            QRScannerOverlay(state.isQRProcessing)
//        } else {
//            // Permission not accepted, and rationale is not currently shown (dialog will handle rationale)
//            if (!state.showCameraRationale) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "Camera permission is required to scan QR codes. Please grant the permission when prompted, or enable it in app settings if you have previously denied it.",
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                }
//            }
//        }

    }

}

@Preview
@Composable
private fun QRScannerScreenPreview() {
    QRCraftTheme {
        QRScannerScreen(
            state = QRScannerState(),
            onAction = {}
        )
    }
}
