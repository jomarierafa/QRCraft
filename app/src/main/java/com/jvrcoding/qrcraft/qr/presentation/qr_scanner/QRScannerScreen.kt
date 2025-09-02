package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.jvrcoding.qrcraft.core.presentation.util.ObserveAsEvents
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.components.CameraPreview
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.components.QRScannerOverlay
import org.koin.androidx.compose.koinViewModel


@Composable
fun QRSCannerScreenRoot(
    onNavigateToPreviewScreen: (QrDetail) -> Unit,
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
                onNavigateToPreviewScreen(event.qrDetail)
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
    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onAction = onAction
        )
        QRScannerOverlay(state.isQRProcessing)


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
