package com.jvrcoding.qrcraft.qr.presentation.qr_scanner.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.onOverlay
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.overlay

@Composable
fun QRScannerOverlay(
    isLoading: Boolean
) {
    val cornerPaintColor = MaterialTheme.colorScheme.primary
    val bgColor = MaterialTheme.colorScheme.overlay

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val parentWidth = this.maxWidth
        val parentHeight = this.maxHeight

        // Calculate the cutout's properties based on parent dimensions
        val cutoutSize = min(parentWidth, parentHeight) * 0.8f
        val cutoutTopY = (parentHeight - cutoutSize) / 2

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Canvas drawing logic uses its own `size`
            val boxSize = size.minDimension * 0.8f
            val boxTopLeftGlobal = Offset((size.width - boxSize) / 2, (size.height - boxSize) / 2)
            val boxRect = Rect(boxTopLeftGlobal, Size(boxSize, boxSize))

            // Screen background
            drawRect(color = bgColor)

            if(!isLoading) {
                // Rounded transparent cutout
                val cornerRadiusPx = 24.dp.toPx()
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = boxRect.topLeft,
                    size = boxRect.size,
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                    blendMode = BlendMode.Clear
                )

                val strokeWidthPx = 4.dp.toPx()
                val armLengthPx = 40.dp.toPx()
                val arcDrawSize = Size(cornerRadiusPx * 2, cornerRadiusPx * 2)
                val arcDrawStroke = Stroke(width = strokeWidthPx)

                fun drawCornerIndicator(
                    arcTopLeftCoord: Offset,
                    arcStartAngleDegrees: Float,
                    horizontalLineStartCoord: Offset,
                    horizontalLineEndCoord: Offset,
                    verticalLineStartCoord: Offset,
                    verticalLineEndCoord: Offset
                ) {
                    drawArc(
                        color = cornerPaintColor,
                        startAngle = arcStartAngleDegrees,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = arcTopLeftCoord,
                        size = arcDrawSize,
                        style = arcDrawStroke
                    )
                    drawLine(
                        color = cornerPaintColor,
                        start = horizontalLineStartCoord,
                        end = horizontalLineEndCoord,
                        strokeWidth = strokeWidthPx
                    )
                    drawLine(
                        color = cornerPaintColor,
                        start = verticalLineStartCoord,
                        end = verticalLineEndCoord,
                        strokeWidth = strokeWidthPx
                    )
                }

                // Top-left corner
                drawCornerIndicator(
                    arcTopLeftCoord = boxRect.topLeft,
                    arcStartAngleDegrees = 180f,
                    horizontalLineStartCoord = boxRect.topLeft + Offset(cornerRadiusPx, 0f),
                    horizontalLineEndCoord = boxRect.topLeft + Offset(armLengthPx, 0f),
                    verticalLineStartCoord = boxRect.topLeft + Offset(0f, cornerRadiusPx),
                    verticalLineEndCoord = boxRect.topLeft + Offset(0f, armLengthPx)
                )

                // Top-right corner
                drawCornerIndicator(
                    arcTopLeftCoord = Offset(boxRect.topRight.x - cornerRadiusPx * 2, boxRect.topRight.y),
                    arcStartAngleDegrees = 270f,
                    horizontalLineStartCoord = boxRect.topRight - Offset(cornerRadiusPx, 0f),
                    horizontalLineEndCoord = boxRect.topRight - Offset(armLengthPx, 0f),
                    verticalLineStartCoord = boxRect.topRight + Offset(0f, cornerRadiusPx),
                    verticalLineEndCoord = boxRect.topRight + Offset(0f, armLengthPx)
                )

                // Bottom-left corner
                drawCornerIndicator(
                    arcTopLeftCoord = Offset(boxRect.bottomLeft.x, boxRect.bottomLeft.y - cornerRadiusPx * 2),
                    arcStartAngleDegrees = 90f,
                    horizontalLineStartCoord = boxRect.bottomLeft + Offset(cornerRadiusPx, 0f),
                    horizontalLineEndCoord = boxRect.bottomLeft + Offset(armLengthPx, 0f),
                    verticalLineStartCoord = boxRect.bottomLeft - Offset(0f, cornerRadiusPx),
                    verticalLineEndCoord = boxRect.bottomLeft - Offset(0f, armLengthPx)
                )

                // Bottom-right corner
                drawCornerIndicator(
                    arcTopLeftCoord = Offset(boxRect.bottomRight.x - cornerRadiusPx * 2, boxRect.bottomRight.y - cornerRadiusPx * 2),
                    arcStartAngleDegrees = 0f,
                    horizontalLineStartCoord = boxRect.bottomRight - Offset(cornerRadiusPx, 0f),
                    horizontalLineEndCoord = boxRect.bottomRight - Offset(armLengthPx, 0f),
                    verticalLineStartCoord = boxRect.bottomRight - Offset(0f, cornerRadiusPx),
                    verticalLineEndCoord = boxRect.bottomRight - Offset(0f, armLengthPx)
                )
            }

        }

        if(!isLoading) {
            Text(
                text = stringResource(R.string.point_your_camera_at_a_qr_code),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onOverlay
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = cutoutTopY - 48.dp)
                    .padding(horizontal = 32.dp)
            )
        } else {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    strokeWidth = 1.5.dp,
                    color = MaterialTheme.colorScheme.onOverlay
                )

                Text(
                    text = stringResource(R.string.loading),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onOverlay
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview(device = Devices.PHONE)
@Composable
private fun QRScannerOverlayPreview() {
    QRCraftTheme {
        QRScannerOverlay(
            isLoading = true
        )
    }
}
