package com.jvrcoding.qrcraft.qr.presentation.data_entry.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftButton
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme

@Composable
fun QRLinkDataForm(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QRTextField(
            state = rememberTextFieldState(),
            hint = stringResource(R.string.url),
            keyboardType = KeyboardType.Uri
        )
        QRCraftButton(
            text = stringResource(R.string.generate_qr_code),
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.primary
        )

    }

}

@Preview
@Composable
private fun QRLinkDataFormPreview() {
    QRCraftTheme {
        QRLinkDataForm()
    }
}