package com.jvrcoding.qrcraft.qr.presentation.scan_result.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.OnSurfaceAlt
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.OnSurfaceDisabled
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 6,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            onTextLayout = { result ->
                if (!isExpanded) {
                    isOverflowing = result.hasVisualOverflow
                }
            }
        )

        if (isOverflowing || isExpanded) {
            Text(
                text = if (isExpanded) "Show less" else "Show more",
                color = if (isExpanded) OnSurfaceDisabled else OnSurfaceAlt,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { isExpanded = !isExpanded }
            )
        }
    }
}

@Preview
@Composable
private fun ExpandableTextPreview() {
    QRCraftTheme {
        ExpandableText(
            "Adipiscing ipsum lacinia tincidunt sed. In risus dui accumsan accumsan quam morbi nulla. Dictum justo metus auctor nunc quam id sed. Urna nisi gravida sed lobortis diam pretium. Adipiscing ipsum lacinia tincidunt sed. In risus dui accumsan accumsan quam morbi nulla. Dictum metus auctor nunc quam id sed. Urna nisi gravida sed lobortis diam pretium."
        )
    }
}
