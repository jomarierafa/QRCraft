package com.jvrcoding.qrcraft.qr.presentation.scan_result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftButton
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftToolbar
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.CopyIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.ShareIcon
import com.jvrcoding.qrcraft.qr.presentation.scan_result.components.ExpandableText
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanResultScreenRoot(viewModel: ScanResultViewModel = koinViewModel()) {
    ScanResultScreen(
        state = viewModel.state
    )
}

@Composable
fun ScanResultScreen(
    state: ScanResultState
) {
    Scaffold(
        topBar = {
            QRCraftToolbar(
                title = stringResource(R.string.scan_result),
                onBackClick = {}
            )
        }
    ) {  innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(innerPadding),
        ) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .offset(y = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "QR Code",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .zIndex(1f)
                )

                Column(
                    modifier = Modifier
                        .offset(y = (-80).dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .widthIn(max = 480.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 100.dp,
                            bottom = 16.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = state.contentType.asString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    ExpandableText(
                        text =  state.contentValue
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QRCraftButton(
                            text = stringResource(R.string.share),
                            icon = ShareIcon,
                            onClick = {},
                            modifier = Modifier
                                .weight(1f)
                        )
                        QRCraftButton(
                            text = stringResource(R.string.copy),
                            icon = CopyIcon,
                            onClick = {},
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun ScanResultScreenPreview() {
    QRCraftTheme {
        ScanResultScreen(
            state = ScanResultState()
        )
    }
}