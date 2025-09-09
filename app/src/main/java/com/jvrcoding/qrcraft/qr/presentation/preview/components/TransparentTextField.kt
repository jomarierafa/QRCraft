package com.jvrcoding.qrcraft.qr.presentation.preview.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.onSurfaceAlt

@Composable
fun TransparentTextField(
    text: String,
    hintText: String? = null,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier
            .wrapContentWidth(),
        textStyle = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        decorationBox = {  innerTextField ->
            Box(
                contentAlignment = Alignment.TopCenter,
            ) {
                if(text.isBlank() && hintText != null) {
                    Text(
                        text = hintText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceAlt.copy(
                                alpha = 0.4f
                            )
                        )
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun TransparentTextFieldPreview() {
    QRCraftTheme {
        TransparentTextField(
            hintText = "Text",
            text = "",
            onValueChange = {},
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}