package com.jvrcoding.qrcraft.qr.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.StarFilledIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.StarIcon
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Text
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.onOverlay
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.onSurfaceAlt
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.onSurfaceDisabled
import com.jvrcoding.qrcraft.qr.presentation.preview.PreviewAction

@Composable
fun HistoryItem(
    iconRes: Int,
    iconTint: Color,
    title: String,
    content: String,
    dateTime: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            tint = iconTint,
            contentDescription = null,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .size(20.dp)
                ) {
                    Icon(
                        imageVector = if(isFavorite) {
                            StarFilledIcon
                        } else StarIcon,
                        contentDescription = stringResource(R.string.go_back),
                        tint =  if(isFavorite) {
                            MaterialTheme.colorScheme.onSurface
                        } else MaterialTheme.colorScheme.onSurfaceDisabled,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceAlt,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = dateTime,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceDisabled
            )
        }
    }
}


@Preview
@Composable
private fun HistoryItemPreview() {
    QRCraftTheme {
        HistoryItem(
            iconRes = R.drawable.ic_text,
            iconTint = Text,
            title = "Text wadawd aw wdawd wdadwa wadawdwdwad awdwawadawd wdawd awdwwd",
            content = "Adipiscing ipsum lacinia tincidunt sed. In risus dui accumsan accumsan quam morbi nulla. Dictum justo metus auctor nunc quam id sed. Urna nisi gravida sed lobortis diam pretium.",
            dateTime = "24 Jun 2025, 14:36",
            isFavorite = true,
            onFavoriteClick = {}
        )
    }
}