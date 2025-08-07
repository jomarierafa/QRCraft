package com.jvrcoding.qrcraft.qr_scanner

import android.Manifest
import android.content.Context
import android.graphics.Rect
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.designsystem.components.QRCraftDialog
import com.jvrcoding.qrcraft.core.designsystem.components.QRCraftSnackBar
import com.jvrcoding.qrcraft.qr_scanner.util.hasCameraPermission
import com.jvrcoding.qrcraft.qr_scanner.util.shouldShowCameraPermissionRationale
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun QRSCannerScreenRoot(
    viewModel: QRScannerViewModel = koinViewModel(),
){
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
    val lifecycleOwner = LocalLifecycleOwner.current
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
                AndroidView(
                    factory = { context ->
                        val previewView = PreviewView(context)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()

                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }

                            val imageAnalysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            val scanner = BarcodeScanning.getClient(
                                BarcodeScannerOptions.Builder()
                                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                                    .build()
                            )

                            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                                val mediaImage = imageProxy.image
                                if (mediaImage != null /*&& state.isScanning*/) {
                                    // Calculate the scan zone based on the image dimensions
                                    val imageWidth = imageProxy.width
                                    val imageHeight = imageProxy.height
                                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees

                                    val uprightImageWidth: Int
                                    val uprightImageHeight: Int
                                    if (rotationDegrees == 90 || rotationDegrees == 270) {
                                        uprightImageWidth = imageHeight // Image is rotated, swap dimensions
                                        uprightImageHeight = imageWidth
                                    } else {
                                        uprightImageWidth = imageWidth
                                        uprightImageHeight = imageHeight
                                    }

                                    val boxSizeRatio = 0.5f
                                    val minDim = if (uprightImageWidth > 0 && uprightImageHeight > 0) kotlin.math.min(uprightImageWidth, uprightImageHeight) else 0
                                    val scanBoxSize = minDim * boxSizeRatio

                                    val scanBoxLeft = (uprightImageWidth - scanBoxSize) / 2f
                                    val scanBoxTop = (uprightImageHeight - scanBoxSize) / 2f
                                    val scanBoxRight = scanBoxLeft + scanBoxSize
                                    val scanBoxBottom = scanBoxTop + scanBoxSize

                                    // This Rect is in the image's coordinate system (after rotation is handled by InputImage)
                                    val scanZoneInImageCoordinates = Rect(
                                        scanBoxLeft.toInt().coerceAtLeast(0),
                                        scanBoxTop.toInt().coerceAtLeast(0),
                                        scanBoxRight.toInt().coerceAtMost(uprightImageWidth),
                                        scanBoxBottom.toInt().coerceAtMost(uprightImageHeight)
                                    )

                                    val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                                    scanner.process(inputImage)
                                        .addOnSuccessListener { barcodes ->
                                            barcodes.firstOrNull { barcode ->
                                                val boundingBox = barcode.boundingBox
                                                // Check if the barcode's bounding box is within our defined scan zone
                                                boundingBox != null && scanZoneInImageCoordinates.contains(boundingBox)
                                            }?.rawValue?.let { value ->
                                                Log.d("QRScanner", "Scanned within bounds: $value")
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("QRScanner", "Barcode scanning failed", exception)
                                        }
                                        .addOnCompleteListener {
                                            imageProxy.close()
                                        }
                                } else {
                                    imageProxy.close()
                                }
                            }

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                        }, ContextCompat.getMainExecutor(context))

                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
                QRScannerOverlay()
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
