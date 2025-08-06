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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.jvrcoding.qrcraft.qr_scanner.util.hasCameraPermission
import com.jvrcoding.qrcraft.qr_scanner.util.shouldShowCameraPermissionRationale
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

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasCameraPermission = perms[Manifest.permission.CAMERA] == true
        val activity = context as ComponentActivity
        val showCameraRationale = activity.shouldShowCameraPermissionRationale()

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

    Box(modifier = Modifier.fillMaxSize()) {
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

                            val boxSizeRatio = 0.5f // Same as QRScannerOverlay
                            // Ensure min is used correctly; uprightImageWidth/Height could be 0 if imageProxy is invalid.
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

//        state.scannedText?.let { result ->
//            Text(
//                text = "Scanned: $result",
//                color = Color.White,
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(16.dp)
//            )
//        }

        if(state.showCameraRationale) {
            QRCraftDialog(
                title = "Camera Required",
                description = "This app cannot function without camera access. To scan QR codes, please grant permission.",
                onDismiss = {},
                primaryButton = {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        shape = RoundedCornerShape(100f),
                        modifier = Modifier.weight(1f)
                    ) {
                        BasicText(
                            text = stringResource(R.string.grant_access),
                            style = MaterialTheme.typography.labelLarge,
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
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                        shape = RoundedCornerShape(100f),
                        modifier = Modifier.weight(1f)
                    ) {
                        BasicText(
                            text = stringResource(R.string.grant_access),
                            style = MaterialTheme.typography.labelLarge,
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
