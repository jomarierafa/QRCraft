package com.jvrcoding.qrcraft.qr.presentation.preview

import android.content.ClipData
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftButton
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftToolbar
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.CopyIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.ShareIcon
import com.jvrcoding.qrcraft.core.presentation.util.ObserveAsEvents
import com.jvrcoding.qrcraft.qr.presentation.preview.components.ExpandableText
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.core.net.toUri
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Link
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.linkBG
import com.jvrcoding.qrcraft.core.presentation.util.shareText
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi
import com.jvrcoding.qrcraft.qr.presentation.preview.components.TransparentTextField

@Composable
fun PreviewScreenRoot(
    viewModel: PreviewViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {

    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    ObserveAsEvents(flow = viewModel.events) { event ->
        when(event) {
            is PreviewEvent.CopyText -> {
                scope.launch {
                    val clipData = ClipData.newPlainText("Qr Value", event.data)
                    clipboard.setClipEntry(clipData.toClipEntry())
                    Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                }
            }
            is PreviewEvent.ShareData -> {
                context.shareText(event.data)
            }
        }
    }

    PreviewScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action) {
                PreviewAction.OnBackIconClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun PreviewScreen(
    state: PreviewState,
    onAction: (PreviewAction) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            QRCraftToolbar(
                title = state.toolbarTitle,
                showBackButton = true,
                onBackClick = {
                    onAction(PreviewAction.OnBackIconClick)
                }
            )
        }
    ) {  innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            val parentWidth = this.maxWidth
            val parentHeight = this.maxHeight

            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(top = parentHeight * 0.1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = state.qrImage?.asImageBitmap()
                        ?: ImageBitmap.imageResource(R.drawable.qr_default),
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

                    TransparentTextField(
                        text = state.title,
                        hintText = state.qrType.title.asString(),
                        onValueChange = { onAction(PreviewAction.OnTitleTextChange(it)) }
                    )

                    when (state.qrType) {
                        QrTypeUi.TEXT -> {
                            ExpandableText(
                                modifier = Modifier.fillMaxWidth(),
                                text = state.contentValue
                            )
                        }

                        QrTypeUi.LINK -> {
                            Text(
                                text = state.contentValue,
                                style = MaterialTheme.typography.labelLarge,
                                color = Link,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .clickable {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            state.contentValue.toUri()
                                        )
                                        context.startActivity(intent)
                                    }
                                    .background(MaterialTheme.colorScheme.linkBG)
                            )
                        }

                        else -> {
                            Text(
                                text = state.contentValue,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QRCraftButton(
                            text = stringResource(R.string.share),
                            icon = ShareIcon,
                            onClick = {
                                onAction(PreviewAction.OnShareButtonClick)
                            },
                            modifier = Modifier
                                .weight(1f)
                        )
                        QRCraftButton(
                            text = stringResource(R.string.copy),
                            icon = CopyIcon,
                            onClick = {
                                onAction(PreviewAction.OnCopyButtonClick)
                            },
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
private fun PreviewScreenPreview() {
    QRCraftTheme {
        PreviewScreen(
            state = PreviewState(
                qrType = QrTypeUi.TEXT,
                contentValue = "hello world\n wwwwertyqwertyqwerty",
//                contentValue = "Adipiscing ipsum lacinia tincidunt sed. In risus dui accumsan accumsan quam morbi nulla. Dictum justo metus auctor nunc quam id sed. Urna nisi gravida sed lobortis diam pretium. Adipiscing ipsum lacinia tincidunt sed. In risus dui accumsan accumsan quam morbi nulla. Dictum metus auctor nunc quam id sed. Urna nisi gravida sed lobortis diam pretium."
            ),
            onAction = {}
        )
    }
}